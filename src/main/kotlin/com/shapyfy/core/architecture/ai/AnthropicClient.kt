package com.shapyfy.core.architecture.ai

import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.models.messages.Message
import com.anthropic.models.messages.MessageCreateParams
import com.anthropic.models.messages.Model
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.math.log
import com.anthropic.client.AnthropicClient as AnthropicSdkClient

@Slf4j
@Component
class AnthropicClient(
    @param:Value("\${anthropic.api-key}") private val apiKey: String,
    private val objectMapper: ObjectMapper
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        val DEFAULT_MODEL: Model = Model.CLAUDE_HAIKU_4_5
    }


    private val client: AnthropicSdkClient by lazy {
        AnthropicOkHttpClient.builder()
            .apiKey(apiKey)
            .build()
    }

    fun <T> sendPrompt(
        prompt: String,
        responseType: Class<T>,
        model: Model = DEFAULT_MODEL,
        maxTokens: Int = 1024,
        temperature: Double = 1.0
    ): T {
        val systemPrompt =
            "You must respond ONLY with valid JSON. Do not include any explanatory text, markdown formatting, or code blocks. Return only the raw JSON object."

        log.info("Anthropic API Input - Model: {}, System: {}, User: {}", model, systemPrompt, prompt)

        val params = MessageCreateParams.builder()
            .model(model)
            .maxTokens(maxTokens.toLong())
            .temperature(temperature)
            .system(systemPrompt)
            .addUserMessage(prompt)
            .build()

        val message = client.messages().create(params)
        val jsonResponse = extractTextContent(message)

        log.info("Anthropic API Output - Response: {}", jsonResponse)

        return parseJsonResponse(jsonResponse, responseType)
    }

    fun <T> sendPromptWithSystem(
        systemPrompt: String,
        userPrompt: String,
        responseType: Class<T>,
        model: Model = DEFAULT_MODEL,
        maxTokens: Int = 1024,
        temperature: Double = 1.0
    ): T {
        val enhancedSystemPrompt = """
            $systemPrompt

            IMPORTANT: You must respond ONLY with valid JSON. Do not include any explanatory text, markdown formatting, or code blocks. Return only the raw JSON object.
        """.trimIndent()

        log.info("Anthropic API Input - Model: {}, System: {}, User: {}", model, enhancedSystemPrompt, userPrompt)

        val params = MessageCreateParams.builder()
            .model(model)
            .maxTokens(maxTokens.toLong())
            .temperature(temperature)
            .system(enhancedSystemPrompt)
            .addUserMessage(userPrompt)
            .build()

        val message = client.messages().create(params)
        val jsonResponse = extractTextContent(message)

        log.info("Anthropic API Output - Response: {}", jsonResponse)

        return parseJsonResponse(jsonResponse, responseType)
    }

    /**
     * Extracts the text content from a Claude Message response.
     */
    private fun extractTextContent(message: Message): String {
        return message.content()
            .mapNotNull { block ->
                block.text().orElse(null)?.text()
            }
            .joinToString("")
    }

    /**
     * Parses the Claude JSON response into the requested type.
     *
     * @throws IllegalArgumentException if the response is empty or cannot be parsed
     */
    private fun <T> parseJsonResponse(jsonResponse: String, responseType: Class<T>): T {
        if (jsonResponse.isBlank()) {
            throw IllegalArgumentException("Anthropic response was empty; expected JSON for ${responseType.simpleName}")
        }

        val sanitized = sanitizeAnthropicJson(jsonResponse)

        return try {
            objectMapper.readValue(sanitized, responseType)
        } catch (ex: Exception) {
            throw IllegalArgumentException(
                "Failed to parse Anthropic response into ${responseType.simpleName}: $jsonResponse",
                ex
            )
        }
    }
}

/**
 * Inline convenience wrapper for {@link AnthropicClient#sendPrompt(String, Class, Model, Int, Double)}
 * that infers the response type from the call site.
 */
inline fun <reified T> AnthropicClient.sendPrompt(
    prompt: String,
    model: Model = AnthropicClient.DEFAULT_MODEL,
    maxTokens: Int = 1024,
    temperature: Double = 1.0
): T = this.sendPrompt(prompt, T::class.java, model, maxTokens, temperature)

/**
 * Inline convenience wrapper for {@link AnthropicClient#sendPromptWithSystem(String, String, Class, Model, Int, Double)}.
 */
inline fun <reified T> AnthropicClient.sendPromptWithSystem(
    systemPrompt: String,
    userPrompt: String,
    model: Model = AnthropicClient.DEFAULT_MODEL,
    maxTokens: Int = 1024,
    temperature: Double = 1.0
): T = this.sendPromptWithSystem(systemPrompt, userPrompt, T::class.java, model, maxTokens, temperature)

internal fun sanitizeAnthropicJson(raw: String): String {
    val trimmed = raw.trim()
    if (!trimmed.startsWith("```")) {
        return trimmed
    }

    val withoutLeadingFence = trimmed
        .removePrefix("```")
        .trimStart()

    val content = if (withoutLeadingFence.startsWith("json", ignoreCase = true)) {
        withoutLeadingFence.substringAfter("\n").trimStart()
    } else {
        withoutLeadingFence
    }

    return content.removeSuffix("```").trim()
}
