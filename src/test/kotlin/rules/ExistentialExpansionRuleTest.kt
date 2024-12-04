package rules

import nl.vu.kai.dl4python.datatypes.Concept
import nl.vu.kai.dl4python.datatypes.ConceptName
import org.junit.jupiter.api.Test
import org.kr.assignment.rules.ConceptWrapper
import org.kr.assignment.rules.ExistentialExpansionRule

class ExistentialExpansionRuleTest {
    @Test
    fun `there are no changes on the set of concepts and roles when there are no existential restriction rules`() {
        val concepts = setOf<Concept>(
            ConceptName("concept1"),
            ConceptName("concept2"),
            ConceptName("concept3")
        )

        val conceptWrapper = ConceptWrapper(concepts, emptySet())
//        val result = ExistentialExpansionRule().applyTo()


    }
}