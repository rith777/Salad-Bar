package rules

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptConjunction
import nl.vu.kai.dl4python.datatypes.ConceptName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kr.assignment.rules.ConceptWrapper
import org.kr.assignment.rules.ConjunctionDecompositionRule
import org.kr.assignment.rules.RuleStatus
import scala.collection.JavaConverters
import scala.collection.Seq
import java.util.*

class ConjunctionDecompositionRuleTest {
    private val rule = ConjunctionDecompositionRule()

    @Test
    fun `there are no changes in the interpretation when there are no conjunctions`() {
        val concepts = mutableSetOf<Concept>(
            ConceptName("concept1"),
            ConceptName("concept2"),
        )

        val conceptWrapper = ConceptWrapper(concepts, emptySet())
        val result = rule.applyTo(conceptWrapper)

        assertThat(result.status).isEqualTo(RuleStatus.NOT_APPLIED)
        assertThat(concepts).hasSize(2)
    }

    @Test
    fun `conjunctions related to a concept are added to the interpretation`() {
        val conjunctions = createConjunctions(2)
        val conceptConjunction = ConceptConjunction(conjunctions)

        val concepts = setOf<Concept>(
            ConceptName("concept1"),
            ConceptName("concept2"),
            conceptConjunction
        )

        val conceptWrapper = ConceptWrapper(concepts, emptySet())
        val result = rule.applyTo(conceptWrapper)

        assertThat(result.status).isEqualTo(RuleStatus.APPLIED)
        assertThat(result.interpretation).hasSize(concepts.size + conjunctions.size())
        assertThat(result.interpretation).containsAnyOf(*conceptConjunction.conjuncts.toTypedArray())
        assertThat(result.interpretation).containsAnyOf(*concepts.toTypedArray())
    }

    @Test
    fun `when all concepts are conjunctions, all related conjunctions are added to the interpretation`() {
        val concepts = setOf<Concept>(
            ConceptConjunction(createConjunctions(1)),
            ConceptConjunction(createConjunctions(3)),
            ConceptConjunction(createConjunctions(5))
        )

        val conceptWrapper = ConceptWrapper(concepts, emptySet())

        val result = rule.applyTo(conceptWrapper)

        assertThat(result.status).isEqualTo(RuleStatus.APPLIED)
        assertThat(result.interpretation).hasSize(12)
    }

    private fun createConjunctions(numberOfConjunctions: Int): Seq<Concept> = List(numberOfConjunctions) {
        ConceptName(UUID.randomUUID().toString())
    }.let { conjunctions ->
        JavaConverters.asScalaIterator<Concept>(conjunctions.iterator()).toSeq()
    }
}
