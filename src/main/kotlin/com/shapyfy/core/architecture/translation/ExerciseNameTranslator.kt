package com.shapyfy.core.architecture.translation

import com.shapyfy.core.domain.exercise.CanonicalizationResult
import com.shapyfy.core.domain.exercise.ExerciseTranslationPort
import com.shapyfy.core.domain.exercise.Exercise
import com.shapyfy.core.domain.Language
import com.shapyfy.core.boundary.exercises.NameNormalizer
import com.shapyfy.core.domain.exercise.TranslationKey
import com.shapyfy.core.domain.exercise.TranslationKeyFactory
import com.shapyfy.core.domain.exercise.TranslationRecord
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExerciseNameTranslator(
    private val translationCatalog: TranslationCatalogPort,
    private val aiTranslationClient: AiTranslationClient
) : ExerciseTranslationPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun canonicalize(rawName: String, preferredLanguage: Language): CanonicalizationResult {
        log.info(
            "Attempt to canonicalize exercise name '{}' (preferred-language={})",
            rawName,
            preferredLanguage.code
        )

        val directMatch = translationCatalog.findByNormalizedValue(rawName)

        if (directMatch != null) {
            log.info(
                "Reusing existing translation for key {} detected-language={}",
                directMatch.translationKey.value,
                directMatch.language.code
            )

            val canonicalName = directMatch.translationKey.value.substringAfter("exercises.")
            return CanonicalizationResult(
                canonicalName = canonicalName,
                detectedLanguage = directMatch.language,
                confidence = 1.0
            )
        }

        // Use AI to translate to English
        val aiResult = aiTranslationClient.detectAndTranslate(rawName)
        val canonicalName = NameNormalizer.normalize(aiResult.englishName)
        log.info(
            "AI canonicalized '{}' -> '{}' (detected-language={}, confidence={})",
            rawName,
            canonicalName,
            aiResult.detectedLanguage.code,
            aiResult.confidence
        )

        return CanonicalizationResult(
            canonicalName = canonicalName,
            detectedLanguage = aiResult.detectedLanguage,
            confidence = aiResult.confidence
        )
    }

    override fun storeTranslations(
        exercise: Exercise,
        rawName: String,
        preferredLanguage: Language,
        detectedLanguage: Language
    ): String {
        log.info(
            "Storing translations for exercise '{}' (preferred={}, detected={})",
            exercise.id,
            preferredLanguage.code,
            detectedLanguage.code
        )
        val translationKey = TranslationKeyFactory.forExercise(exercise.canonicalName)

        val translations = buildTranslations(
            translationKey = translationKey,
            canonicalName = exercise.canonicalName.value,
            rawName = rawName,
            preferredLanguage = preferredLanguage,
            detectedLanguage = detectedLanguage
        )

        translationCatalog.saveAll(translations)
        log.info(
            "Persisted {} translations for key {}",
            translations.size,
            translationKey.value
        )

        // Return the localized name for the preferred language from what we just stored
        val localizedName = translations
            .find { it.language == preferredLanguage }
            ?.value
            ?: translations.find { it.language == Language.EN }?.value
            ?: exercise.canonicalName.value.replace("_", " ").replaceFirstChar { it.uppercase() }

        log.info(
            "Returning localized name '{}' for exercise '{}' in language {}",
            localizedName,
            exercise.id,
            preferredLanguage.code
        )

        return localizedName
    }

    override fun localize(exercise: Exercise, language: Language): String {
        log.info(
            "Attempt to localize exercise '{}' for language {}",
            exercise.id,
            language.code
        )
        val translationKey = TranslationKeyFactory.forExercise(exercise.canonicalName)
        val translations = translationCatalog.fetchValues(
            language,
            setOf(translationKey)
        )

        return translations[translationKey]
            ?: exercise.canonicalName.value.replace("_", " ").replaceFirstChar { it.uppercase() }
                .also {
                    log.info(
                        "Fallback localization used for exercise '{}' and language {}",
                        exercise.id,
                        language.code
                    )
                }
    }

    private fun buildTranslations(
        translationKey: TranslationKey,
        canonicalName: String,
        rawName: String,
        preferredLanguage: Language,
        detectedLanguage: Language
    ): List<TranslationRecord> {
        val translations = mutableListOf<TranslationRecord>()
        val normalizedCanonical = NameNormalizer.normalize(canonicalName)
        val rawIsDifferentFromCanonical = rawName != normalizedCanonical

        val englishDisplayName = canonicalName.replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

        translations += TranslationRecord(
            translationKey = translationKey,
            language = Language.EN,
            value = englishDisplayName,
            normalizedValue = normalizedCanonical
        )

        // 2. Determine which language(s) we need AI to translate to
        val allLanguages = Language.values().toSet()
        val languagesAlreadyHave = mutableSetOf(Language.EN)  // We always have English

        // If user provided a non-English input and AI agrees, use their original input
        val userProvidedValidTranslation =
            preferredLanguage != Language.EN &&
            preferredLanguage == detectedLanguage &&
            rawIsDifferentFromCanonical

        if (userProvidedValidTranslation) {
            translations += TranslationRecord(
                translationKey = translationKey,
                language = preferredLanguage,
                value = rawName,
                normalizedValue = rawName
            )
            languagesAlreadyHave += preferredLanguage
        }

        val languagesToTranslate = allLanguages - languagesAlreadyHave

        // Iterate through each language and translate one at a time
        languagesToTranslate.forEach { language ->
            log.info("Translating '{}' to {}", englishDisplayName, language.code)

            val translatedName = aiTranslationClient.translate(
                englishName = englishDisplayName,
                targetLanguage = language
            )

            translations += TranslationRecord(
                translationKey = translationKey,
                language = language,
                value = translatedName,
                normalizedValue = NameNormalizer.normalize(translatedName)
            )
        }

        return translations
    }
}
