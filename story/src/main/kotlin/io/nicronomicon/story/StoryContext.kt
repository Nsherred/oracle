package io.nicronomicon.story

import kotlinx.serialization.Serializable

@Serializable
data class StoryContext(
    val application: String,
    val feature: String,
    val persona: String
)