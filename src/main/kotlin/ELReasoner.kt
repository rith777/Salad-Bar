package org.kr.assignment

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptConjunction
import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.datatypes.Ontology
import org.kr.assignment.rules.*


class ELReasoner(
    private val ontology: Ontology = Ontology(),
) {

    private val inferenceRules: List<InferenceRule> by lazy { initializeInferenceRules() }

    private fun computeSubsumers(conceptWrapper: ConceptWrapper): Set<Concept> {
        var changed = true
        while (changed) {
            changed = applyRules(conceptWrapper)
        }

        return conceptWrapper.concepts[conceptWrapper.targetConceptId] ?: emptySet()
    }

    private fun applyRules(
        conceptWrapper: ConceptWrapper
    ): Boolean = inferenceRules.fold(false) { changed, rule ->
        val res = rule.applyTo(conceptWrapper)
        changed or res
    }

    fun computeSubsumersOf(concept: Concept): Set<Concept> {
        if (concept !in ontology.conceptNames) {
            throw InvalidConceptException(concept)
        }

        return computeSubsumers(ConceptWrapper.initializeWith(concept))
            .let(::cleanUpSubsumers)
    }

    private fun cleanUpSubsumers(subsumers: Set<Concept>): Set<Concept> {
        val conjunctions = subsumers.filterIsInstance<ConceptConjunction>()
            .map { it.conjuncts }
            .flatten()
            .filterIsInstance<ConceptName>()



        return (subsumers.filter { it is ConceptName || it == ELFactory.getTop() } +
                conjunctions).toSet()
    }

    private fun initializeInferenceRules() = listOf(
        TopClassAssignmentInferenceRule(),
        ConjunctionCompositionRule(ontology.subConcepts),
        ConjunctionDecompositionRule(ontology.subConcepts),
        ExistentialExpansionRule(ontology),
        ExistentialIntroductionRule(),
        SubsumptionPropagationRule(ontology)
    )
}

private fun String.asConceptName() = ConceptName(this)

class InvalidConceptException(concept: Concept) : Exception(
    "Concept $concept does not exist on given ontology. " +
            "Please, enter a valid concept and try again."
)