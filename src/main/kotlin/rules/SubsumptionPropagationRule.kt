package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.*

/**
 * Equivalent to ⊑-rule: If d has C assigned and C ⊑ D ∈ T , then also assign D to d.
 */
class SubsumptionPropagationRule(val ontology: Ontology) : InferenceRule {
    private val subsumptions = preprocess()

    override fun applyTo(conceptWrapper: ConceptWrapper): Boolean {
        val assigned: Set<Concept> = conceptWrapper.concepts[conceptWrapper.targetConceptId] ?: emptySet()

        val newConcepts = newConcepts(assigned)

        conceptWrapper.concepts[conceptWrapper.targetConceptId]?.addAll(newConcepts)

        return newConcepts.isNotEmpty()
    }

    private fun newConcepts(assigned: Set<Concept>): List<Concept> {
        val relatedSubsumtions: Set<Concept> = assigned.mapNotNull { concept -> subsumptions[concept] }
            .flatten()
            .toSet()

        val generalConceptInclusionSubsumptions = relatedSubsumtions.filterIsInstance<GeneralConceptInclusion>()
            .filter { it.lhs() in assigned && it.rhs() !in assigned }
            .map { it.rhs() }

        val conjunctionSubsumptions = relatedSubsumtions.filterIsInstance<ConceptConjunction>()
            .map { it.conjuncts }
            .flatten()
            .filterNot { it in assigned }

        return (generalConceptInclusionSubsumptions + conjunctionSubsumptions)
    }

    private fun allGeneralInclusionAxioms(): Sequence<GeneralConceptInclusion> {
        val equivalenceAsGeneralConceptInclusion = ontology.tbox().axioms.filterIsInstance<EquivalenceAxiom>()
            .asGeneralInclusion()

        val generalConceptInclusions = ontology.tbox().axioms.filterIsInstance<GeneralConceptInclusion>()

        return (generalConceptInclusions + equivalenceAsGeneralConceptInclusion)
            .asSequence()
    }

    private fun preprocess(): Map<Concept, Set<Concept>> {
        val classHierarchy: Map<Concept, List<Concept>> = buildClassHierarchy()

        val transitiveClosure = ontology.conceptNames.associateWith { concept ->
            ((classHierarchy[concept] ?: emptySet()) + concept)
        }

        return classHierarchy.mapValues { (_, reachableClasses) ->
            transitiveClosure.keys.fold(reachableClasses) { acc, currentConcept ->
                if (reachableClasses.contains(currentConcept)) {
                    acc + (transitiveClosure[currentConcept] ?: emptySet())
                } else {
                    acc
                }
            }
        }.map { (key, values) -> key to values.toSet() }.toMap()
    }

    private fun buildClassHierarchy(): Map<Concept, List<Concept>> = allGeneralInclusionAxioms()
        .filterIsInstance<GeneralConceptInclusion>()
        .groupBy { it.lhs() } as Map<Concept, List<Concept>>

}

private fun Collection<EquivalenceAxiom>.asGeneralInclusion(): List<GeneralConceptInclusion> =
    this.filterIsInstance<EquivalenceAxiom>()
        .map {
            setOf(
                GeneralConceptInclusion(it.concepts.first(), it.concepts.last()),
                GeneralConceptInclusion(it.concepts.last(), it.concepts.first())
            )
        }.flatten()