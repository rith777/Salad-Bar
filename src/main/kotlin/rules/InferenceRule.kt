package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.Role

fun interface InferenceRule {
    fun applyTo(conceptWrapper: ConceptWrapper): Result
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
    val concepts: Set<Concept>,
    val successors: Set<Role>
)