package io.nicronomicon.story

import com.aallam.openai.api.completion.Choice
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StoryAITest {

    @Test
    fun execute() {
        val openAI = mockk<OpenAI>()
        val uut = StoryAI(openAI)

        val storyContext = StoryContext(
            "Car maintenance estimator",
            "Shop locator",
            "Single Mom"
        )
        val choice = Choice(response, 0)
        coEvery { openAI.completion(any()) } returns TextCompletion("", 10L, ModelId("test"), listOf(choice))
        val result = runBlocking {
            uut.execute(storyContext)
        }
        assertEquals(
            "As a single Mom, I want to find a shop close to my location so that I can save time and money on car maintenance.",
            result.userStory
        )
        assertEquals(
            listOf(
                "Single Mom is able to find a shop near her location",
                "Estimates of cost of car repairs are accurate and up-to-date",
            ),
            result.acceptanceCriteria
        )
        assertEquals(
            listOf(
                "What criteria are used in order to locate a shop near Single Mom’s location?",
                "Are there any additional features that could be added to the shop locator to make it more convenient?",
                "Are there any discounts or special pricing for Single Moms that could be included?"
            ),
            result.decisions
        )
    }

    private val response = """
        |USER STORY: As a single Mom, I want to find a shop close to my location so that I can save time and money on car maintenance.
        |ACCEPTANCE CRITERIA: 
        |- Single Mom is able to find a shop near her location
        |- Estimates of cost of car repairs are accurate and up-to-date 
        |DECISIONS TO BE MADE:
        |- What criteria are used in order to locate a shop near Single Mom’s location?
        |- Are there any additional features that could be added to the shop locator to make it more convenient?
        |- Are there any discounts or special pricing for Single Moms that could be included?
    """.trimMargin()
}