package org.kr.assignment.rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept

/** equivalent to ⊓-rule 2: If d has C and D assigned, assign also C ⊓ D to d.
 */
class ConjunctionCompositionRule(private val allConceptsWithinOntology: Set<Concept>) : InferenceRule {
    override fun applyTo(conceptWrapper: ConceptWrapper): Boolean {
        val assigned = conceptWrapper.concepts[conceptWrapper.targetConceptId] ?: mutableSetOf()

        val newConcepts = assigned.withIndex().flatMap { (i, first) ->
            assigned.drop(i + 1).mapNotNull { second ->
                val newConjunction = ELFactory.getConjunction(first, second)

                if (newConjunction in allConceptsWithinOntology && newConjunction !in assigned) newConjunction else null
            }
        }

        conceptWrapper.concepts[conceptWrapper.targetConceptId]?.addAll(newConcepts)

        return newConcepts.isNotEmpty()
    }

}