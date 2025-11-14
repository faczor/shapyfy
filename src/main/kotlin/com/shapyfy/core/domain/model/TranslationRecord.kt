package com.shapyfy.core.domain.model

data class TranslationRecord(
    val translationKey: TranslationKey,
    val language: Language,
    val value: String,
    val normalizedValue: String,
    val category: TranslationCategory
)
