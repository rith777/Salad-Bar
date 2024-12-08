package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptName

/**
 * ∃-rule 2: If d has an r -successor with C assigned, add ∃r .C to d.
 */
class ExistentialIntroductionRule : InferenceRule {
    override fun applyTo(conceptWrapper: ConceptWrapper): Boolean {
        val assigned = conceptWrapper.concepts[conceptWrapper.targetConceptId] ?: mutableSetOf()

        val newConcepts: Set<Concept> = conceptWrapper.successors[conceptWrapper.targetConceptId]
            ?.filterValues { it is ConceptName && it in assigned }
            ?.map { (role, concept) -> ELFactory.getExistentialRoleRestriction(role, concept) }
            ?.toSet() ?: mutableSetOf()

        conceptWrapper.concepts[conceptWrapper.targetConceptId]?.addAll(newConcepts)

        return newConcepts.isNotEmpty()
    }
}