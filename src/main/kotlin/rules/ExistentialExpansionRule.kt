package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.ExistentialRoleRestriction
import nl.vu.kai.dl4python.datatypes.Role

/**
 * Equivalent to ∃-rule 1: If d has ∃r .C assigned, and there is no r -successor with C assigned, add
 * a new r -successor to d and assign C to it.
 */
class ExistentialExpansionRule : InferenceRule {

    override fun applyTo(conceptWrapper: ConceptWrapper): Result {
        val restrictionFillers = conceptWrapper.concepts.filterIsInstance<ExistentialRoleRestriction>()
            .map { it.role() }
            .toSet()

        return Result(determineStatus(restrictionFillers), conceptWrapper.concepts, restrictionFillers)
    }

    private fun determineStatus(restrictionFillers: Set<Role>): RuleStatus {
        val status = if (restrictionFillers.isEmpty()) RuleStatus.NOT_APPLIED else RuleStatus.APPLIED
        return status
    }
}