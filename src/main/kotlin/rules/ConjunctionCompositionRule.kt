package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept

/** equivalent to ⊓-rule 2: If d has C and D assigned, assign also C ⊓ D to d.
 */
class ConjunctionCompositionRule(private val allConceptsWithinOntology: Set<Concept>) : InferenceRule {

    /**
     * Converts unique pairs of [Concept] within [conceptWrapper] into conjunctions and add them to interpretation
     * in case they are included in [allConceptsWithinOntology].
     *
     *  Nodes set as owl:intersectionOf are considered existing conjunctions in [allConceptsWithinOntology]
     *
     *  @param conceptWrapper
     *  @return [Result] with status set as[RuleStatus.APPLIED] if top concept was added to interpretation. Otherwise,
     *         the status is set to RuleStatus.NOT_APPLIED. The set of concepts is updated with the new valid
     *         conjunctions. A conjunction is considered valid when it exists within the set of
     *         [allConceptsWithinOntology]
     */
    override fun applyTo(conceptWrapper: ConceptWrapper): Result {
        val conceptPairs = conceptWrapper.concepts.map { item ->
            conceptWrapper.concepts.mapNotNull { if (it == item) null else item to it }
        }.flatten()

        val newConcepts = conceptPairs.mapNotNull { (first, second) ->
            val newConcept = ELFactory.getConjunction(first, second)

            if (newConcept in allConceptsWithinOntology) newConcept else null
        }

        return if (newConcepts.isEmpty()) {
            Result(RuleStatus.NOT_APPLIED, conceptWrapper.concepts)
        } else {
            Result(RuleStatus.APPLIED, (conceptWrapper.concepts + newConcepts))
        }
    }
}