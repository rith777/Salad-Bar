package org.kr.assignment

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.datatypes.Ontology
import org.kr.assignment.rules.InferenceRule


class ELReasoner(private val inferenceRules: List<InferenceRule>) {

    private fun Concept.isSubsumerOf(submee: Concept): Boolean {
        val relatedConcepts = inferenceRules.fold(setOf(submee)) { acc, inferenceRule ->
            acc + inferenceRule.applyTo(acc).interpretation
        }

        return this in relatedConcepts
    }

    fun computeSubsumers(subsumer: Concept, ontology: Ontology): Set<Concept> {
        if (subsumer !in ontology.conceptNames) {
            throw InvalidConceptException("There is no concept with name $subsumer on given ontology.")
        }

        val allConcepts: Set<Concept> = ontology.conceptNames

        return allConcepts.fold(setOf(subsumer)) { accumulator, concept ->
            if (subsumer.isSubsumerOf(concept)) accumulator + subsumer else accumulator
        }
    }
}

private fun String.asConceptName() = ConceptName(this)

class InvalidConceptException(message: String) : Exception(message)