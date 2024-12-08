package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.*

/**
 * Equivalent to ⊑-rule: If d has C assigned and C ⊑ D ∈ T , then also assign D to d.
 */
class SubsumptionPropagationRule(val tBox: TBox, val allConceptsOnOntology: Set<Concept>) : InferenceRule {
    private val conceptInclusionAxioms: Sequence<GeneralConceptInclusion> =
        (tBox.axioms.filterIsInstance<GeneralConceptInclusion>() +
                convertEquivalentAxiomsToGeneralConceptInclusion(tBox.axioms.filterIsInstance<EquivalenceAxiom>())
                ).asSequence()

    override fun applyTo(conceptWrapper: ConceptWrapper): Boolean {
        val assigned = conceptWrapper.concepts[conceptWrapper.targetConceptId] ?: mutableSetOf()

        val subclassesOfConcepts = conceptInclusionAxioms.filter { it.lhs() in assigned }
            .map { it.rhs() }
            .filter { it !in assigned }
            .toSet()

        val newConcepts = subclassesOfConcepts.map {
            when (it) {
                is ConceptName -> setOf(it)
                is ConceptConjunction -> it.conjuncts.toSet()
                else -> emptySet()
            }
        }.flatten().filter { it !in assigned }

        conceptWrapper.concepts[conceptWrapper.targetConceptId]?.addAll(newConcepts)


        return newConcepts.isNotEmpty()
    }

    private fun convertEquivalentAxiomsToGeneralConceptInclusion(
        axioms: List<EquivalenceAxiom>
    ) = tBox.axioms.filterIsInstance<EquivalenceAxiom>()
        .map {
            setOf(
                GeneralConceptInclusion(it.concepts.first(), it.concepts.last()),
                GeneralConceptInclusion(it.concepts.last(), it.concepts.first())
            )
        }.flatten()
}