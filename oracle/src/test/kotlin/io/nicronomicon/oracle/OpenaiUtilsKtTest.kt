package io.nicronomicon.oracle

import com.aallam.openai.api.embedding.Embedding
import io.nicronomicon.ecs.ID
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test

class OpenaiUtilsKtTest {
    @Test
    fun `verify `() {
        val prompt = Message(
            ID.makeRandomID(),
            Speaker.User,
            Clock.System.now(),
            Embedding(emptyList(), 1),
            "me"
        ).asResponsePrompt("- summary")
        println(prompt.text)
    }
}