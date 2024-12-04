package org.kr.assignment

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptName
import nl.vu.kai.dl4python.datatypes.Ontology
import org.kr.assignment.rules.ConceptWrapper
import org.kr.assignment.rules.InferenceRule
import org.kr.assignment.rules.Result
import org.kr.assignment.rules.RuleStatus


class ELReasoner(private val inferenceRules: List<InferenceRule>) {

    private fun applyRules(concepts: Set<Concept>): Set<Concept> {
        return applyRules(Result(RuleStatus.APPLIED, concepts))
    }

    private fun applyRules(previousResult: Result): Set<Concept> {
        if (previousResult.status == RuleStatus.NOT_APPLIED)
            return previousResult.interpretation

        val result = inferenceRules.fold(previousResult.copy(status = RuleStatus.NOT_APPLIED)) { acc, inferenceRule ->
            val result = inferenceRule.applyTo(ConceptWrapper(acc.interpretation, acc.successors))
            val status = if (acc.status == RuleStatus.APPLIED) acc.status else result.status

            Result(
                status = status,
                interpretation = result.interpretation + acc.interpretation,
                successors = result.successors + acc.successors
            )
        }

        return applyRules(result)
    }

    fun computeSubsumers(subsumer: Concept, ontology: Ontology): Set<Concept> {
        if (subsumer !in ontology.conceptNames) {
            throw InvalidConceptException("There is no concept with name $subsumer on given ontology.")
        }

        return applyRules(Result(RuleStatus.APPLIED, setOf(subsumer)))
    }
}

private fun String.asConceptName() = ConceptName(this)

class InvalidConceptException(message: String) : Exception(message)