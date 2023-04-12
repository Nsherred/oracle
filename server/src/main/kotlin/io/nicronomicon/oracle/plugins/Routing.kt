package io.nicronomicon.oracle.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.nicronomicon.oracle.OracleAI
import io.nicronomicon.oracle.Prompt
import io.nicronomicon.story.StoryAI
import io.nicronomicon.story.StoryContext
import io.nicronomicon.utils.createLogger

fun Application.configureRouting(oracleAI: OracleAI, storyAI: StoryAI) {
    val log = createLogger<Application>()
    routing {
        post("/oracle") {
            val prompt = call.receive<Prompt>()
            val response = oracleAI.run(prompt)
            call.respond(response.message)
        }
        post("/story") {
            log.info("wut")
            val context = call.receive<StoryContext>()

            val story = storyAI.execute(context)
            call.respond(story)
        }
    }
}
