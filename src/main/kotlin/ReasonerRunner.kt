@file:JvmName("ReasonerRunner")

package org.kr.assignment

import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.owlapi.OWLParser
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException


fun main(args: Array<String>) {
    if (args.size != 2) {
        throw RuntimeException("Expected 2 arguments: ontology file path and target class")
    }

    val filePath = args[0]
    val className = args[1]

    try {
        val owlParser = OWLParser()

        val subsumers = owlParser.parseFile(filePath).let { ontology ->
            ELReasoner(ontology).computeSubsumersOf(ConceptName(className))
        }

        println("Found ${subsumers.size} for class $className: \n${subsumers.joinToString(separator = "\n")}")
    } catch (exception: OWLOntologyCreationIOException) {
        println("Could not find $filePath. Try again with a valid file path.")
    }
}