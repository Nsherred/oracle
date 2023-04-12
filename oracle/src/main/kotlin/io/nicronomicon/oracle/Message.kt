package io.nicronomicon.oracle

import com.aallam.openai.api.embedding.Embedding
import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class Message(
    override val id: ID,
    val speaker: Speaker,
    val time: Instant,
    val embedding: Embedding,
    val message: String
) : Entity

fun Message.toBlock(): String = "$speaker: $message"