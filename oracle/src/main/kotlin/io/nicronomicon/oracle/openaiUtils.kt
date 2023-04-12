package io.nicronomicon.oracle

import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.embedding.Embedding
import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlin.math.sqrt

fun calculateCosineSimilarity(
    a: List<Double>,
    b: List<Double>
): Double {
    val dotProduct = a.zip(b).sumOf { it.first * it.second }
    val magnitudeA = sqrt(a.sumOf { it * it })
    val magnitudeB = sqrt(b.sumOf { it * it })
    return dotProduct / (magnitudeA * magnitudeB)
}

fun List<Double>.magnitude() = sqrt(sumOf { it * it })

fun getSimilarity(
    a: List<Double>,
    b: List<Double>
): Double {
    return calculateCosineSimilarity(a, b) / (a.magnitude() * b.magnitude())
}

suspend fun OpenAI.getGptEmbedding(content: List<String>): List<Embedding> {
    return embeddings(EmbeddingRequest(ModelId("text-embedding-ada-002"), content))
}

suspend fun OpenAI.getCompletion(prompt: Prompt): TextCompletion {
    // engine='text-davinci-003', temp=0.0, top_p=1.0, tokens=400, freq_pen=0.0, pres_pen=0.0,
    val completionRequest = CompletionRequest(
        model = ModelId("text-davinci-003"),
        maxTokens = 400,
        temperature = 0.0,
        topP = 1.0,
        prompt = prompt.text,
        echo = false,
        stop = listOf("${Speaker.User}:", "${Speaker.Oracle}:", "USER STORY:")
    )
    return completion(completionRequest)
}

fun List<Message>.asSummaryPrompt(): Prompt {
    return Prompt(
        """
        |Write detailed notes of the following in a hyphenated list format like "- "
        |
        |
        |
        |${joinToString("\n") { it.toBlock() }}
        |
        |
        |
        |NOTES:
""".trimMargin()
    )
}

fun Message.asResponsePrompt(summary: String?): Prompt {
    return Prompt(
        """
        |I am a chatbot named Oracle. My goals are to help other achieve their goals, and increase their understanding.
        |I will read the conversation notes and recent messages, and then I will provide a concise and detailed answer.
        |
        |
        |
        |The following are notes from earlier conversations with USER:
        |$summary
        |
        |
        |
        |I will now provide a concise and detailed response:
        |User: ${this.message} 
        |Oracle:
""".trimMargin()
    )
}

fun List<Message>.asResponsePrompt(summary: String?): Prompt {
    return Prompt(
        """
        |I am a chatbot named Oracle. My goals are to help other achieve their goals, and increase their understanding.
        |I will read the conversation notes and recent messages, and then I will provide a concise and detailed answer.
        |
        |
        |
        |The following are notes from earlier conversations with USER:
        |$summary
        |
        |
        |
        |The following are the most recent messages in the conversation:
        |${joinToString("\n") { it.toBlock() }}
        |
        |
        |
        |I will now provide a concise and detailed response:
        |Oracle:
""".trimMargin()
    )
}
