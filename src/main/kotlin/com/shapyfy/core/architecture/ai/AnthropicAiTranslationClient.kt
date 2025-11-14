package com.shapyfy.core.architecture.ai

import com.fasterxml.jackson.annotation.JsonProperty
import com.shapyfy.core.architecture.translation.AiTranslationClient
import com.shapyfy.core.architecture.translation.AiTranslationResult
import com.shapyfy.core.domain.model.Language
import com.shapyfy.core.domain.model.NameNormalizer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

/**
 * Anthropic-backed implementation that relies on Claude to detect the input language
 * and provide an English exercise name. The AnthropicClient already enforces JSON-only responses.
 */
@Component
class AnthropicAiTranslationClient(
    private val anthropicClient: AnthropicClient
) : AiTranslationClient {

    private val logger = LoggerFactory.getLogger(AnthropicAiTranslationClient::class.java)

    override fun detectAndTranslate(name: String): AiTranslationResult {
        logger.info("Attempt to translate exercise name '{}' via Anthropic", name)
        return runCatching { aiTranslate(name) }
            .onFailure { logger.error("Anthropic translation failed for '{}', using heuristic fallback", name, it) }
            .getOrElse { heuristicFallback(name) }
    }

    private fun aiTranslate(name: String): AiTranslationResult {
        val prompt = buildPrompt(name)
        val response = anthropicClient.sendPrompt<TranslationResponse>(prompt)

        val detectedLanguage = Language.from(response.detectedLanguage)
        val englishName = response.englishName.ifBlank { name }
        val confidence = response.confidence?.let { min(1.0, max(0.0, it)) } ?: DEFAULT_CONFIDENCE

        return AiTranslationResult(
            detectedLanguage = detectedLanguage,
            englishName = englishName,
            confidence = confidence
        )
    }

    private fun buildPrompt(rawName: String): String = """
        You are an expert multilingual fitness coach. Analyze the provided exercise name
        and determine (1) which human language it is written in and (2) the best English-equivalent
        exercise name a trainer would recognize. If the phrase already appears to be English, keep it natural.

        Return a JSON object with:
        - detected_language: ISO 639-1 two-letter lowercase code (example: "en", "pl").
        - english_name: Title-cased English exercise name (e.g., "Bench Press").
        - confidence: float between 0 and 1 describing how confident you are in the mapping.

        Example response:
        {
          "detected_language": "pl",
          "english_name": "Squat",
          "confidence": 0.92
        }

        Exercise name: "$rawName"
    """.trimIndent()

    private data class TranslationResponse(

        @JsonProperty("detected_language")
        val detectedLanguage: String?,

        @JsonProperty("english_name")
        val englishName: String,

        val confidence: Double?
    )

    private fun heuristicFallback(name: String): AiTranslationResult {
        val trimmed = name.trim()
        val lower = trimmed.lowercase(Locale.getDefault())

        val isInDictionary = FALLBACK_DICTIONARY.containsKey(lower)
        val hasPolishCharacters = POLISH_CHARACTERS.containsMatchIn(lower)

        val detectedLanguage = if (isInDictionary || hasPolishCharacters) {
            Language.PL
        } else {
            Language.EN
        }

        val englishName = when {
            isInDictionary -> FALLBACK_DICTIONARY.getValue(lower)
            detectedLanguage == Language.PL -> trimmed.split(" ")
                .joinToString(" ") { word ->
                    NameNormalizer.normalize(word).replaceFirstChar { it.titlecase(Locale.getDefault()) }
                }
            else -> trimmed
        }

        val confidence = if (isInDictionary) 0.95 else DEFAULT_CONFIDENCE
        return AiTranslationResult(
            detectedLanguage = detectedLanguage,
            englishName = englishName,
            confidence = confidence
        )
    }

    private companion object {
        const val DEFAULT_CONFIDENCE = 0.6
        val POLISH_CHARACTERS = Regex("[ąćęłńóśźż]")
        val FALLBACK_DICTIONARY = mapOf(
            "wyciskanie na ławce" to "Bench Press",
            "przysiady" to "Squat",
            "martwy ciąg" to "Deadlift",
            "podciąganie" to "Pull Up"
        )
    }
}
