package io.nicronomicon.oracle

import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import io.mockk.coEvery
import io.mockk.mockk
import io.nicronomicon.ecs.storage.InMemoryStorage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OracleAITest {

    @Test
    fun `runs oracle`() {
        val openAI = mockk<OpenAI>()
        coEvery { openAI.completion(any()) } returns TextCompletion(
            "",
            10L,
            ModelId("f"),
            listOf(mockk(relaxed = true))
        )
        coEvery { openAI.embeddings(any()) } returns listOf(mockk(relaxed = true))
        val oracleAI = OracleAI(openAI, InMemoryStorage(), InMemoryStorage())
        val prompt = Prompt("Hello")
        runBlocking {
            val response = oracleAI.run(prompt)
            assertEquals("", response.message)
        }
    }
}