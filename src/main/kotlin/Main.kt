package org.kr.assignment

import nl.vu.kai.dl4python.datatypes.DLHelpers
import nl.vu.kai.dl4python.owlapi.OWLParser
import nl.vu.kai.dl4python.reasoning.DLReasoners

fun main() {
    // TODO(): receive ontology path and class name via console

    val owlParser = OWLParser()

    val ontologyFileName = "pizza.owl"
    val file = Thread.currentThread().contextClassLoader.getResource(ontologyFileName)?.file

    val ontology = owlParser.parseFile(file!!)

    println("Total concepts ${ontology.conceptNames.size}")
    DLHelpers.convert2binaryConjunctions(ontology)

    ontology.conceptNames.forEach { concept ->
        val elSubsumers = ELReasoner(ontology).computeSubsumersOf(concept)
        val elkSubsumers = DLReasoners.getELKReasoner().apply { setOntology(ontology) }.getSubsumers(concept)
        val hermitSubsumers = DLReasoners.getHermiTReasoner().apply { setOntology(ontology) }.getSubsumers(concept)

        println("======================================================")
        println("concept name $concept")
        println("EL reasoner: ${elSubsumers.size}")
        println("Hermit reasoner: ${hermitSubsumers.size}")
        println("ELK reasoner: ${elkSubsumers.size}")
        println("======================================================")
    }
}

