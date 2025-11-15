package com.shapyfy.core.architecture.translation

import com.shapyfy.core.domain.exercise.CanonicalizationResult
import com.shapyfy.core.domain.exercise.ExerciseTranslationPort
import com.shapyfy.core.domain.exercise.Exercise
import com.shapyfy.core.domain.Language
import com.shapyfy.core.boundary.exercises.NameNormalizer
import com.shapyfy.core.domain.exercise.TranslationCategory
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

        val directMatch = translationCatalog.findByNormalizedValue(
            TranslationCategory.EXERCISES,
            rawName
        )

        if (directMatch != null) {
            log.info(
                "Reusing existing translation for key {} detected-language={}",
                directMatch.translationKey.value,
                directMatch.language.code
            )

            val canonicalName = directMatch.translationKey.value.substringAfter("${TranslationCategory.EXERCISES.value}.")
            return CanonicalizationResult(
                canonicalName = canonicalName,
                detectedLanguage = directMatch.language,
                confidence = 1.0,
                isExistingTranslation = true
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
            confidence = aiResult.confidence,
            isExistingTranslation = false
        )
    }

    override fun storeTranslations(
        exercise: Exercise,
        rawName: String,
        preferredLanguage: Language,
        detectedLanguage: Language
    ) {
        log.info(
            "Storing translations for exercise '{}' (preferred={}, detected={})",
            exercise.id,
            preferredLanguage.code,
            detectedLanguage.code
        )
        val translationKey = TranslationKeyFactory.forExercise(exercise.name)

        val translations = buildTranslations(
            translationKey = translationKey,
            canonicalName = exercise.name,
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
    }

    override fun localize(exercise: Exercise, language: Language): String {
        log.info(
            "Attempt to localize exercise '{}' for language {}",
            exercise.id,
            language.code
        )
        val translationKey = TranslationKeyFactory.forExercise(exercise.name)
        val translations = translationCatalog.fetchValues(
            TranslationCategory.EXERCISES,
            language,
            setOf(translationKey)
        )

        return translations[translationKey]
            ?: exercise.name.replace("_", " ").replaceFirstChar { it.uppercase() }
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

        // Always store English
        val englishDisplayName = canonicalName.replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

        translations += TranslationRecord(
            translationKey = translationKey,
            language = Language.EN,
            value = englishDisplayName,
            normalizedValue = normalizedCanonical,
            category = TranslationCategory.EXERCISES
        )

        // Store preferred language only when detector agrees with the caller
        val shouldStorePreferred =
            preferredLanguage != Language.EN &&
                preferredLanguage == detectedLanguage &&
                rawName != normalizedCanonical

        if (shouldStorePreferred) {
            translations += TranslationRecord(
                translationKey = translationKey,
                language = preferredLanguage,
                value = rawName,
                normalizedValue = rawName,
                category = TranslationCategory.EXERCISES
            )
        }

        // Store detected language if different
        if (detectedLanguage != Language.EN &&
            detectedLanguage != preferredLanguage &&
            rawName != normalizedCanonical
        ) {
            translations += TranslationRecord(
                translationKey = translationKey,
                language = detectedLanguage,
                value = rawName,
                normalizedValue = rawName,
                category = TranslationCategory.EXERCISES
            )
        }

        return translations
    }
}
