package org.kr.assignment

import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.datatypes.Ontology
import nl.vu.kai.dl4python.owlapi.OWLParser
import nl.vu.kai.dl4python.reasoning.DLReasoners
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.stream.Stream
import kotlin.io.path.extension
import kotlin.time.measureTimedValue

val ALLOWED_EXTENSIONS = arrayOf("xml", "owl", "owx")

data class ExperimentResult(
    val fileName: String,
    val conceptName: ConceptName,
    val elCounter: Int,
    val elELapsedTimeInMilliseconds: Long,
    val elkCounter: Int,
    val elkELapsedTimeInMilliseconds: Long,
    val hermitCounter: Int,
    val hermitELapsedTimeInMilliseconds: Long,
)

val worker: ExecutorService = Executors.newFixedThreadPool(30)

fun main() {


    val tasks: MutableList<Future<List<ExperimentResult>>>? = getOntologyPaths()!!.map { filePath ->
        worker.submit(Callable { processOntologyFile(filePath.toFile()) })
    }.toList()

    val results: List<ExperimentResult> = tasks?.map { it.get() }?.flatten() ?: emptyList()

    println("storing results")

    worker.shutdown()
    println("finished")
}

fun processOntologyFile(file: File): List<ExperimentResult> {
    println("process file ${file.name}")
    val owlParser = OWLParser()
    val ontology = owlParser.parseFile(file)

    val results = ontology.conceptNames.mapIndexed { index, concept ->
        println("processing file ${file.name} - concept index: $index")
        computeSubsumers(concept, ontology, file.name)
    }

    FileOutputStream("${file.name}.csv").apply { writeCsv(results) }

    println("finished processing file ${file.name}")
    return results
}

fun OutputStream.writeCsv(data: List<ExperimentResult>) {
    val writer = bufferedWriter()
    writer.write(
        ("filename, concept, elCounter, elELapsedTimeInMilliseconds, elkCounter, elkELapsedTimeInMilliseconds, " +
                "hermitCounter, hermitELapsedTimeInMilliseconds").trimMargin()
    )
    writer.newLine()
    data.forEach {
        writer.write(
            "${it.fileName}, ${it.conceptName}, ${it.elCounter},  ${it.elELapsedTimeInMilliseconds}," +
                    "${it.elkCounter},  ${it.elkELapsedTimeInMilliseconds},  ${it.hermitCounter}, " +
                    "${it.hermitELapsedTimeInMilliseconds}"
        )
        writer.newLine()
    }
    writer.flush()
    writer.close()
}

fun computeSubsumers(
    concept: ConceptName,
    ontology: Ontology, fileName: String
): ExperimentResult {
    println("computing susumer for classs $concept")
    val elSubsumers = measureTimedValue {
        worker.submit(Callable { ELReasoner(ontology).computeSubsumersOf(concept).size })
    }

    val elkSubsumers = measureTimedValue {
        worker.submit(Callable {
            DLReasoners.getELKReasoner().apply { setOntology(ontology) }.getSubsumers(concept).size
        })

    }

    val hermitSubsumers = measureTimedValue {
        worker.submit(Callable {
            DLReasoners.getHermiTReasoner().apply { setOntology(ontology) }.getSubsumers(concept).size
        })
    }

    return ExperimentResult(
        fileName,
        concept,
        elSubsumers.value.get(),
        elSubsumers.duration.inWholeMilliseconds,
        elkSubsumers.value.get(),
        elkSubsumers.duration.inWholeMilliseconds,
        hermitSubsumers.value.get(),
        hermitSubsumers.duration.inWholeMilliseconds,
    )
}

private fun getOntologyPaths(): Stream<Path>? {
    val absolutePath = Paths.get("").toAbsolutePath().toString()
    val resourcesPath = Paths.get(absolutePath, "src/main/resources").toAbsolutePath()

    return Files.walk(resourcesPath)
        .filter(Files::isRegularFile)
        .filter { it.extension in ALLOWED_EXTENSIONS }
}