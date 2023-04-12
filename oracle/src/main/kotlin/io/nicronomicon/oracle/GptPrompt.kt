package io.nicronomicon.oracle

import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import kotlinx.serialization.Serializable

@Serializable
class GptPrompt(override val id: ID, val prompt: String, val response: String) : Entity