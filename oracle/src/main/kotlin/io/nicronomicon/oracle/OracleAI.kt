package io.nicronomicon.oracle

import com.aallam.openai.api.embedding.Embedding
import com.aallam.openai.client.OpenAI
import io.nicronomicon.ecs.ID
import io.nicronomicon.ecs.storage.Storage
import io.nicronomicon.utils.createLogger
import kotlinx.datetime.Clock

class OracleAI(
    private val openAI: OpenAI,
    private val storage: Storage<Message>,
    private val promptStorage: Storage<GptPrompt>
) {

    suspend fun run(prompt: Prompt): Message {
        // get embedding so we can find similar previous conversations
        val embedding = openAI.getGptEmbedding(listOf(prompt.text)).first()
        val conversation = getConversation()
        val current = storage.save(
            Message(ID.makeRandomID(), Speaker.User, Clock.System.now(), embedding, prompt.text)
        )

        val memories = getMemories(embedding, conversation, 10)
        // TODO - fetch declarative memories (facts, wikis, KB, company data, internet, etc)
        log.info("memories: ${memories.joinToString("\n") { it.message.message }}")
        val summary = summarize(memories)
        log.info(summary)
        val recentMessages = getRecentMessages()
        val responsePrompt = current.asResponsePrompt(summary)
        val response = executeResponse(responsePrompt)
        promptStorage.save(GptPrompt(ID.makeRandomID(), responsePrompt.text, response.message))
        return response
    }

    private suspend fun getConversation(): List<Message> {
        return storage.getAll().toList()
    }

    private suspend fun getRecentMessages(): List<Message> {
        return getConversation().sortedBy { it.time }.take(5)
    }

    private suspend fun summarize(memories: List<Memory>): String? {
        if(memories.isEmpty()) {
            return null
        }
        val prompt = memories.map { it.message }.asSummaryPrompt()
        log.info(prompt.text)
        return this.openAI.getCompletion(prompt).choices.first().text
    }

    private suspend fun executeResponse(prompt: Prompt): Message {
        val response = openAI.getCompletion(prompt).choices.first().text
        val embedding = openAI.getGptEmbedding(listOf(response)).first()
        return storage.save(
            Message(ID.makeRandomID(), Speaker.Oracle, Clock.System.now(), embedding, response)
        )
    }

    private fun getMemories(
        embedding: Embedding,
        messages: List<Message>,
        count: Int
    ): List<Memory> {
        val validMemories = mutableListOf<Memory>()
        for (conversation in messages) {
            if (conversation.embedding == embedding) {
                continue
            }
            val similarity = getSimilarity(embedding.embedding, conversation.embedding.embedding)
            if (similarity > 0.5) {
                validMemories.add(Memory(conversation, similarity))
            }
        }
        return validMemories.sortedByDescending { it.similarity }.take(count)
    }

    companion object {
        private val log = createLogger<OracleAI>()
    }

}