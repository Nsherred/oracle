package io.nicronomicon.story

import com.aallam.openai.client.OpenAI
import io.nicronomicon.oracle.Prompt
import io.nicronomicon.oracle.getCompletion
import io.nicronomicon.utils.createLogger

class StoryAI(private val openAI: OpenAI) {
    suspend fun execute(storyContext: StoryContext): StoryResult {
        log.info("Executing story for context: ${storyContext.application}")
        val prompt = storyContext.toPrompt()
        val response = openAI.getCompletion(prompt).choices.first { it.text.isNotBlank() }.text
        log.info(response)
        return response.toStoryResult()
    }

    private fun StoryContext.toPrompt(): Prompt {
        return Prompt("""
        |Write a professional USER STORY, VALIDATION CRITERIA, and DECISIONS TO BE MADE. For an application based on the following APP, FEATURE, and PERSONA:
        |
        |APP: Sales CRM
        |FEATURE: Sales Report
        |PERSONA: Sales Professional
        |USER STORY: Sales Professional I want to generate reports so that I can take a decision on the marketing strategy for the upcoming quarter.
        |ACCEPTANCE CRITERIA: 
        |- Sales Professional is able to generate reports
        |- Generated Reports contain data on sales over the last quarter
        |DECISIONS:
        |- What data is required for be report to be useful?
        |- Is the report always locked to the last quarter?
        |- Is there any additional data that could be included in the report?
        |
        |APP: $application
        |FEATURE: $feature
        |PERSONA: $persona
        |USER STORY:
        """.trimMargin())
    }

    private fun String.toStoryResult(): StoryResult {
        val lines = this.lines()
        val userStory = lines[0].substringAfter("USER STORY:").trim()
        val acceptanceCriteria = (2..3).map { lines[it].trimStart('-').trim() }
        val decisions = (5..7).map { lines[it].trimStart('-').trim() }
        return StoryResult(userStory, acceptanceCriteria, decisions)
    }
    companion object{
        private val log = createLogger<StoryAI>()
    }
}