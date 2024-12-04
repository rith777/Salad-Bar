package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory

/**
 * ∃-rule 2: If d has an r -successor with C assigned, add ∃r .C to d.
 */
class ExistentialIntroductionRule : InferenceRule {
    override fun applyTo(conceptWrapper: ConceptWrapper): Result {
        val newConcepts = conceptWrapper.successors.map { successor ->
            conceptWrapper.concepts.mapNotNull { concept -> if (concept == successor) null else successor to concept }
        }.flatten().map { (successor, concept) -> ELFactory.getExistentialRoleRestriction(successor, concept) }

        val status = if (newConcepts.isEmpty()) RuleStatus.NOT_APPLIED else RuleStatus.APPLIED

        return Result(status, conceptWrapper.concepts + newConcepts, conceptWrapper.successors)
    }
}