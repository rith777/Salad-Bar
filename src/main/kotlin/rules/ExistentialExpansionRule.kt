package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.ExistentialRoleRestriction
import nl.vu.kai.dl4python.datatypes.Ontology
import java.util.*

/**
 * Equivalent to ∃-rule 1: If d has ∃r .C assigned, and there is no r -successor with C assigned, add
 * a new r -successor to d and assign C to it.
 */
class ExistentialExpansionRule(val ontology: Ontology) : InferenceRule {
    override fun applyTo(conceptWrapper: ConceptWrapper): Boolean {
        val assigned = conceptWrapper.concepts[conceptWrapper.targetConceptId] ?: mutableSetOf()

        val targetSuccessorSize = conceptWrapper.successors[conceptWrapper.targetConceptId]?.size ?: 0

        val roleRestriction = assigned.filterIsInstance<ExistentialRoleRestriction>()

        val successorID = UUID.randomUUID()
        roleRestriction.forEach { concept ->
            if (concept in assigned) {
                conceptWrapper.successors[conceptWrapper.targetConceptId]?.put(concept.role(), concept)
            } else {
                conceptWrapper.successors.getOrPut(successorID, ::mutableMapOf)[concept.role()] = concept.filler()
            }
        }

        return (conceptWrapper.successors[conceptWrapper.targetConceptId]?.size ?: 0) > targetSuccessorSize ||
                (conceptWrapper.successors[successorID]?.size ?: 0) > 0
    }
}