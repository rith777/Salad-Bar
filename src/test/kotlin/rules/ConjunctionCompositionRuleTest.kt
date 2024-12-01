package rules

import nl.vu.kai.dl4python.ELFactory
import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kr.assignment.rules.ConjunctionCompositionRule
import org.kr.assignment.rules.RuleStatus

class ConjunctionCompositionRuleTest {
    @Test
    fun `test sm`() {
        val interpretation = setOf<Concept>(
            ConceptName("concept1"),
            ConceptName("concept2"),
            ConceptName("concept3")
        )

        val existingConjunction = ELFactory.getConjunction(interpretation.first(), interpretation.last())
        val allConceptsWithinOntology = interpretation + existingConjunction

        val result = ConjunctionCompositionRule(allConceptsWithinOntology).applyTo(interpretation)

        assertThat(result.status).isEqualTo(RuleStatus.APPLIED)
        assertThat(result.interpretation).hasSize(4)
        assertThat(result.interpretation).contains(existingConjunction)
        assertThat(result.interpretation).containsAll(interpretation)
    }
}