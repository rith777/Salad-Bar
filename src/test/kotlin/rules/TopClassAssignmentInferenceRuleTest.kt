package rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kr.assignment.rules.RuleStatus
import org.kr.assignment.rules.TopClassAssignmentInferenceRule

class TopClassAssignmentInferenceRuleTest {
    private val rule = TopClassAssignmentInferenceRule()

    private val top = ELFactory.getTop()

    @Test
    fun `it adds top concept to interpretation if it is missing`() {
        val testConcept = ConceptName("test")
        val anotherTestConcept = ConceptName("anotherTest")

        val interpretation: MutableSet<Concept> = mutableSetOf(testConcept, anotherTestConcept)

        val result = rule.applyTo(interpretation)

        assertThat(result.interpretation).hasSize(3)
        assertThat(result.interpretation).containsOnly(testConcept, anotherTestConcept, top)
        assertThat(result.status).isEqualTo(RuleStatus.APPLIED)
    }

    @Test
    fun `it does not add top concept to interpretation it it is already present`() {
        val testConcept = ConceptName("test")

        val interpretation: MutableSet<Concept> = mutableSetOf(testConcept, top)

        val result = rule.applyTo(interpretation)

        assertThat(result.interpretation).isEqualTo(interpretation)
        assertThat(result.status).isEqualTo(RuleStatus.NOT_APPLIED)
    }
}