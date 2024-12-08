package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.Role
import java.util.*

fun interface InferenceRule {
    fun applyTo(conceptWrapper: ConceptWrapper): Boolean
}

enum class RuleStatus {
    APPLIED, NOT_APPLIED
}

data class Result(
    val status: RuleStatus = RuleStatus.APPLIED,
    val interpretation: Set<Concept>,
    val successors: Set<Role> = emptySet()
)

data class ConceptWrapper(
    val targetConceptId: UUID,
    val concepts: MutableMap<UUID, MutableSet<Concept>>,
    val successors: MutableMap<UUID, MutableMap<Role, Concept>>
) {
    companion object {
        fun initializeWith(concept: Concept): ConceptWrapper {
            val targetConceptId = UUID.randomUUID()

            return ConceptWrapper(
                targetConceptId,
                concepts = mutableMapOf(targetConceptId to mutableSetOf(concept)),
                successors = mutableMapOf(targetConceptId to mutableMapOf())
            )
        }
    }
}