package io

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.TopConcept
import nl.vu.kai.dl4python.owlapi.OWLParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kr.assignment.io.OntologyLoader
import java.io.InputStream
import java.nio.file.Path

class OntologyLoaderTest() {

    @Test
    fun `it loads an ontology file`() {
//        val uri = this::class.java.classLoader.getResource("pizza.owl")?.toURI()!!
//
//        OWLParser().parseFile(Path.of(uri).toFile())
        ELFactory.getTop()

        val file: InputStream = this::class.java.classLoader.getResourceAsStream("pizza.owl")!!

        val ontologyLoader = OntologyLoader()

        val a = ontologyLoader.loadOntology(file)


        assertThat(1).isOdd()
    }
}