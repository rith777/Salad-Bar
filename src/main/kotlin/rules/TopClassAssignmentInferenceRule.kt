package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory

/**
 * Equivalent to EL-completion logic ⊤-rule
 */
class TopClassAssignmentInferenceRule : InferenceRule {
    private val top = ELFactory.getTop()

    override fun applyTo(conceptWrapper: ConceptWrapper): Boolean {
        return conceptWrapper.concepts[conceptWrapper.targetConceptId]?.add(top) ?: false
    }
}