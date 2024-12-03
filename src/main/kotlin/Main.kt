package org.kr.assignment

import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.datatypes.DLHelpers
import nl.vu.kai.dl4python.owlapi.OWLParser
import org.kr.assignment.rules.ConjunctionCompositionRule
import org.kr.assignment.rules.ConjunctionDecompositionRule
import org.kr.assignment.rules.TopClassAssignmentInferenceRule

fun main() {
    // TODO(): receive ontology path and class name via console

    val owlParser = OWLParser()

    val ontologyFileName = "pizza.owl"
    val file = Thread.currentThread().contextClassLoader.getResource(ontologyFileName)?.file

    val ontology = owlParser.parseFile(file!!)

    DLHelpers.convert2binaryConjunctions(ontology)

    println(ontology.conceptNames)

    val className = "SpicyPizzaEquivalent"
    val subsumer = ConceptName("\"${className}\"")

    val rules = listOf(
        TopClassAssignmentInferenceRule(),
        ConjunctionCompositionRule(ontology.conceptNames),
        ConjunctionDecompositionRule()
    )

    val submees = ELReasoner(rules).computeSubsumers(subsumer, ontology)
    println("subsumees of $className: \n ${submees.joinToString(separator = "\n")}")
}