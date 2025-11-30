package com.shapyfy.core.architecture.translation

import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.exercise.TranslationKey
import com.shapyfy.core.domain.exercise.TranslationRecord

interface TranslationCatalogPort {
    fun findByNormalizedValue(normalizedValue: String): TranslationRecord?
    fun findAllByKey(translationKey: TranslationKey): List<TranslationRecord>
    fun save(record: TranslationRecord)
    fun saveAll(records: Collection<TranslationRecord>)
    fun fetchValues(language: Language, keys: Collection<TranslationKey>): Map<TranslationKey, String>
}

data class AiTranslationResult(
    val detectedLanguage: Language,
    val englishName: String,
    val confidence: Double
)

interface AiTranslationClient {
    fun detectAndTranslate(name: String): AiTranslationResult

    fun translate(englishName: String, targetLanguage: Language): String
}
