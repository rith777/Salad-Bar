package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory

/**
 * Equivalent to EL-completion logic ⊤-rule
 */
class TopClassAssignmentInferenceRule : InferenceRule {
    private val top = ELFactory.getTop()

    /**
     * Adds Top concept to interpretations if not yet present
     *
     * @param conceptWrapper wraps set of concepts and successor roles.
     * @return [Result]  with status set as[RuleStatus.APPLIED] if top concept was added to interpretation. Otherwise,
     *         the status is set to RuleStatus.NOT_APPLIED. The set of concepts is updated with top concept(⊤) in case
     *         the rule is successfully applied.
     */
    override fun applyTo(conceptWrapper: ConceptWrapper): Result {
        if (top !in conceptWrapper.concepts) {
            return Result(RuleStatus.APPLIED, (conceptWrapper.concepts + top))
        }
        return Result(RuleStatus.NOT_APPLIED, conceptWrapper.concepts)
    }
}