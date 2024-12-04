package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptConjunction

/**
 * Equivalent to ⊓-rule 1:  If d has C ⊓ D assigned, assign also C and D to d
 */
class ConjunctionDecompositionRule : InferenceRule {

    /**
     * Includes all conjunctions related each [Concept] included in the [conceptWrapper] to the [conceptWrapper] itself.
     * The rule is applied when at least one of the concepts included in the [conceptWrapper]
     * has type [ConceptConjunction]
     *
     * @param conceptWrapper
     * @return [Result] with status set as[RuleStatus.APPLIED] if top concept was added to interpretation. Otherwise,
     *         the status is set to RuleStatus.NOT_APPLIED. The set of concepts is updated with the new valid
     *         conjunctions. A conjunction is considered valid when it exists within the set of
     *         [allConceptsWithinOntology]
     */
    override fun applyTo(conceptWrapper: ConceptWrapper): Result {
        val conjuncts: List<Concept> = conceptWrapper.concepts.filterIsInstance<ConceptConjunction>()
            .flatMap { it.conjuncts }

        return if (conjuncts.isNotEmpty()) {
            Result(RuleStatus.APPLIED, (conceptWrapper.concepts + conjuncts))
        } else Result(RuleStatus.NOT_APPLIED, conceptWrapper.concepts)
    }
}