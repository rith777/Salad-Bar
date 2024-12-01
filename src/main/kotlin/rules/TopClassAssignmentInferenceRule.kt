package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept

/**
 * Equivalent to EL-completion logic ⊤-rule
 */
class TopClassAssignmentInferenceRule : InferenceRule {
    private val top = ELFactory.getTop()

    /**
     * Adds Top concept to interpretations if not yet present
     *
     * @param interpretation
     * @return true if top concept was added to interpretation. Otherwise, it returns false.
     */
    override fun applyTo(interpretation: Set<Concept>): Result {


        if (top !in interpretation) {
            return Result(RuleStatus.APPLIED, (interpretation + top))
        }
        return Result(RuleStatus.NOT_APPLIED, interpretation)
    }
}