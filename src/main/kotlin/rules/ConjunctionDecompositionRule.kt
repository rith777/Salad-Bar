package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptConjunction

/**
 * Equivalent to ⊓-rule 1:  If d has C ⊓ D assigned, assign also C and D to d
 */
class ConjunctionDecompositionRule : InferenceRule {

    /**
     * Includes all conjunctions related each [Concept] included in the [interpretation] to the [interpretation] itself.
     * The rule is applied when at least one of the concepts included in the [interpretation]
     * has type [ConceptConjunction]
     *
     * @param interpretation
     * @return result with the updated interpretation if rule was applied. The original interpretation is returned if
     * the rule is not applied.
     */
    override fun applyTo(interpretation: Set<Concept>): Result {
        val conjuncts: List<Concept> = interpretation.filterIsInstance<ConceptConjunction>()
            .flatMap { it.conjuncts }

        return if (conjuncts.isNotEmpty()) {
            Result(RuleStatus.APPLIED, (interpretation + conjuncts))
        } else Result(RuleStatus.NOT_APPLIED, interpretation)
    }
}