package com.shapyfy.core.domain.exercise

import com.shapyfy.core.domain.Language

data class TranslationRecord(
    val translationKey: TranslationKey,
    val language: Language,
    val value: String,
    val normalizedValue: String,
    val category: TranslationCategory
)
