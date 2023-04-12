package io.nicronomicon.story

import kotlinx.serialization.Serializable

@Serializable
class StoryResult(
    val userStory: String,
    val acceptanceCriteria: List<String>,
    val decisions: List<String>
)