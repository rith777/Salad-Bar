package org.kr.assignment.rules

import nl.vu.kai.dl4python.datatypes.Concept

fun interface InferenceRule {
    fun applyTo(interpretation: Set<Concept>): Result
}

enum class RuleStatus {
    APPLIED, NOT_APPLIED
}

data class Result(
    val status: RuleStatus,
    val interpretation: Set<Concept>,
)