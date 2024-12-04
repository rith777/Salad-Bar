package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.datatypes.GeneralConceptInclusion
import nl.vu.kai.dl4python.datatypes.TBox

/**
 * Equivalent to ⊑-rule: If d has C assigned and C ⊑ D ∈ T , then also assign D to d.
 */
class SubsumptionPropagationRule(val tBox: TBox, val allConceptsOnOntology: Set<Concept>) : InferenceRule {
    /**
     * It adds subsumtions to set of concepts if applicable
     *
     * @param tBox: ontology's terminology box.
     * @param [conceptWrapper]
     */
    override fun applyTo(conceptWrapper: ConceptWrapper): Result {
        val subclassesOfConcepts = tBox.axioms.asSequence().filterIsInstance<GeneralConceptInclusion>()
            .filter { it.lhs() in conceptWrapper.concepts && it.lhs() in allConceptsOnOntology }
            .map { it.rhs() }
            .filterIsInstance<ConceptName>()
            .toSet()

        val newConcepts = conceptWrapper.concepts + subclassesOfConcepts

        val status = if (newConcepts != conceptWrapper.concepts) RuleStatus.APPLIED else RuleStatus.NOT_APPLIED

        return Result(status, conceptWrapper.concepts + subclassesOfConcepts, conceptWrapper.successors)
    }

}