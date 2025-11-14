package com.shapyfy.core.architecture.translation

import com.shapyfy.core.domain.model.Language
import com.shapyfy.core.domain.model.TranslationCategory
import com.shapyfy.core.domain.model.TranslationKey
import com.shapyfy.core.domain.model.TranslationRecord

/**
 * Architecture-layer ports for translation services.
 * These are NOT domain ports - they're infrastructure concerns for i18n.
 */

/**
 * Port for storing and retrieving translations for localization.
 */
interface TranslationCatalogPort {
    fun findByNormalizedValue(category: TranslationCategory, normalizedValue: String): TranslationRecord?
    fun findAllByKey(category: TranslationCategory, translationKey: TranslationKey): List<TranslationRecord>
    fun save(record: TranslationRecord)
    fun saveAll(records: Collection<TranslationRecord>)
    fun fetchValues(category: TranslationCategory, language: Language, keys: Collection<TranslationKey>): Map<TranslationKey, String>
}

/**
 * Result from AI translation service.
 */
data class AiTranslationResult(
    val detectedLanguage: Language,
    val englishName: String,
    val confidence: Double
)

/**
 * Port for AI-based language detection and translation service.
 */
interface AiTranslationClient {
    fun detectAndTranslate(name: String): AiTranslationResult
}
