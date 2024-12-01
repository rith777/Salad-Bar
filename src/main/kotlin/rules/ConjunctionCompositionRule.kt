package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept

/** equivalent to ⊓-rule 2: If d has C and D assigned, assign also C ⊓ D to d.
 */
class ConjunctionCompositionRule(private val allConceptsWithinOntology: Set<Concept>) : InferenceRule {

    /**
     * Converts unique pairs of [Concept] within [interpretation] into conjunctions and add them to interpretation
     * in case they are included in [allConceptsWithinOntology].
     *
     *  Nodes set as owl:intersectionOf are considered existing conjunctions in [allConceptsWithinOntology]
     *
     *  @param interpretation
     *  @return result with the updated interpretation if rule was applied. The original interpretation is returned if
     * the rule is not applied.
     */
    override fun applyTo(interpretation: Set<Concept>): Result {
        val conceptPairs = interpretation.map { item ->
            interpretation.mapNotNull { if (it == item) null else item to it }
        }.flatten()

        val newConcepts = conceptPairs.mapNotNull { (first, second) ->
            val newConcept = ELFactory.getConjunction(first, second)

            if (newConcept in allConceptsWithinOntology) newConcept else null
        }

        return if (newConcepts.isEmpty()) {
            Result(RuleStatus.NOT_APPLIED, interpretation)
        } else {
            Result(RuleStatus.APPLIED, (interpretation + newConcepts))
        }
    }
}