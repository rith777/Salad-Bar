package rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.ConceptName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kr.assignment.rules.ConceptWrapper
import org.kr.assignment.rules.RuleStatus
import org.kr.assignment.rules.TopClassAssignmentInferenceRule

class TopClassAssignmentInferenceRuleTest {
    private val rule = TopClassAssignmentInferenceRule()

    private val top = ELFactory.getTop()

    @Test
    fun `it adds top concept to interpretation if it is missing`() {
        val testConcept = ConceptName("test")
        val anotherTestConcept = ConceptName("anotherTest")

        val concepts = setOf(testConcept, anotherTestConcept)
        val conceptWrapper = ConceptWrapper(concepts, emptySet())

        val result = rule.applyTo(conceptWrapper)

        assertThat(result.interpretation).hasSize(3)
        assertThat(result.interpretation).containsOnly(testConcept, anotherTestConcept, top)
        assertThat(result.status).isEqualTo(RuleStatus.APPLIED)
    }

    @Test
    fun `it does not add top concept to interpretation it it is already present`() {
        val testConcept = ConceptName("test")

        val concepts = setOf(testConcept, top)
        val conceptWrapper = ConceptWrapper(concepts, emptySet())

        val result = rule.applyTo(conceptWrapper)

        assertThat(result.interpretation).isEqualTo(concepts)
        assertThat(result.status).isEqualTo(RuleStatus.NOT_APPLIED)
    }
}