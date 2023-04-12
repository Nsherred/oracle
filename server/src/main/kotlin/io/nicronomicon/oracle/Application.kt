package io.nicronomicon.oracle

import com.aallam.openai.client.OpenAI
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.nicronomicon.ecs.storage.fileSystemStorage
import io.nicronomicon.oracle.plugins.configureRouting
import io.nicronomicon.story.StoryAI
import io.nicronomicon.utils.defaultJson
import java.nio.file.Path

fun main(args: Array<String>): Unit = EngineMain.main(args)


fun Application.module() {
    val openAIKey = environment.config.propertyOrNull("ktor.security.openAI")?.getString()
        ?: throw IllegalStateException("OpenAI key not found")

    val messageRawPath = environment.config.propertyOrNull("ktor.filesystem.messages")?.getString()
        ?: throw IllegalStateException("Message storage path not found")
    val promptRawPath = environment.config.propertyOrNull("ktor.filesystem.prompts")?.getString()
        ?: throw IllegalStateException("Prompt storage path not found")
    val openAI = OpenAI(openAIKey)

    val messagePath = Path.of(messageRawPath)
    val promptPath = Path.of(promptRawPath)
    val messageStorage = Message::class.fileSystemStorage(messagePath)
    val promptStorage = GptPrompt::class.fileSystemStorage(promptPath)
    val oracleAI = OracleAI(openAI, messageStorage, promptStorage)
    val storyAI = StoryAI(openAI)

    install(ContentNegotiation) {
        json(defaultJson)
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }
    configureRouting(oracleAI, storyAI)
}
