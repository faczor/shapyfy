package com.shapyfy.core.architecture.ai

import kotlin.test.Test
import kotlin.test.assertEquals

class AnthropicClientTest {

    @Test
    fun `should strip markdown fences`() {
        val raw = """
            ```json
            {
              "detected_language": "en",
              "english_name": "Barbell Press",
              "confidence": 0.95
            }
            ```
        """.trimIndent()

        val sanitized = sanitizeAnthropicJson(raw)

        val expected = """
            {
              "detected_language": "en",
              "english_name": "Barbell Press",
              "confidence": 0.95
            }
        """.trimIndent()

        assertEquals(expected, sanitized)
    }

    @Test
    fun `should return trimmed response when no fences present`() {
        val raw = "\n\n { \"key\": \"value\" }  "

        assertEquals("{ \"key\": \"value\" }", sanitizeAnthropicJson(raw))
    }
}
