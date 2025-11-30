package com.shapyfy.core.architecture.ai

import com.fasterxml.jackson.annotation.JsonProperty
import com.shapyfy.core.architecture.translation.AiTranslationClient
import com.shapyfy.core.architecture.translation.AiTranslationResult
import com.shapyfy.core.domain.Language
import com.shapyfy.core.boundary.exercises.NameNormalizer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

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

    override fun translate(englishName: String, targetLanguage: Language): String {
        logger.info("Attempt to translate '{}' to {}", englishName, targetLanguage.code)
        return runCatching { aiTranslateSingle(englishName, targetLanguage) }
            .onFailure {
                logger.error("Anthropic translation failed for '{}' to {}, using heuristic fallback", englishName, targetLanguage.code, it)
            }
            .getOrElse { heuristicTranslateSingle(englishName, targetLanguage) }
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

    private fun aiTranslateSingle(englishName: String, targetLanguage: Language): String {
        val prompt = buildSingleLanguagePrompt(englishName, targetLanguage)
        val response = anthropicClient.sendPrompt<SingleLanguageTranslationResponse>(prompt)
        return response.translation.ifBlank { englishName }
    }

    private fun buildSingleLanguagePrompt(englishName: String, targetLanguage: Language): String {
        val languageName = when (targetLanguage) {
            Language.EN -> "English"
            Language.PL -> "Polish"
        }

        return """
            You are an expert multilingual fitness coach. Translate the following English exercise name
            to $languageName. Provide the natural, commonly-used translation that trainers would recognize.

            Return a JSON object with a "translation" field containing the translated name.
            The translated name should be title-cased (e.g., "Bench Press").

            Example response for Polish:
            {
              "translation": "Przysiady"
            }

            Exercise name: "$englishName"
            Target language: $languageName (${targetLanguage.code})
        """.trimIndent()
    }

    private data class SingleLanguageTranslationResponse(
        val translation: String
    )

    private fun heuristicTranslateSingle(englishName: String, targetLanguage: Language): String {
        return when (targetLanguage) {
            Language.EN -> englishName
            Language.PL -> {
                // Check reverse dictionary (EN -> PL)
                FALLBACK_DICTIONARY.entries.find { it.value.equals(englishName, ignoreCase = true) }?.key
                    ?: englishName  // Fallback to English if no translation found
            }
        }
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
