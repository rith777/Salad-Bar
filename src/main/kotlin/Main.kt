package org.kr.assignment

import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.datatypes.DLHelpers
import nl.vu.kai.dl4python.datatypes.Ontology
import nl.vu.kai.dl4python.owlapi.OWLParser
import nl.vu.kai.dl4python.reasoning.DLReasoners
import org.kr.assignment.rules.*

fun main() {
    // TODO(): receive ontology path and class name via console

    val owlParser = OWLParser()

    val ontologyFileName = "pizza.owl"
    val file = Thread.currentThread().contextClassLoader.getResource(ontologyFileName)?.file

    val ontology = owlParser.parseFile(file!!)


    val a = Ontology(ontology.tbox(), ontology.abox(), ontology.rbox())

    DLHelpers.convert2binaryConjunctions(ontology)

    println(ontology.conceptNames)

    val className = "IceCream"
    val subsumer = ConceptName("\"${className}\"")
//    val a: Map<ConceptName, Set<Concept>>  = ontology.subConcepts.groupBy { it.conceptNames() }.toMap()
    val rules = listOf(
        TopClassAssignmentInferenceRule(),
        ConjunctionCompositionRule(ontology.subConcepts),
        ConjunctionDecompositionRule(),
        ExistentialExpansionRule(),
        ExistentialIntroductionRule(),
        SubsumptionPropagationRule(ontology.tbox(), ontology.subConcepts)
    )

    val submees = ELReasoner(rules).computeSubsumers(subsumer, ontology)
    println("subsumees of $className: \n ${submees.joinToString(separator = "\n")}")

    val elkReasoner = DLReasoners.getELKReasoner()
    elkReasoner.setOntology(ontology)

    println("expected subsumers: ${elkReasoner.getSubsumers(subsumer)}")
}