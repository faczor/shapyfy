package com.shapyfy.core.domain.model

@JvmInline
value class TranslationKey(val value: String) {
    init {
        require(value.isNotBlank()) { "Translation key cannot be blank" }
    }
}

enum class TranslationCategory(val value: String) {
    EXERCISES("exercises");

    companion object {
        fun fromValue(value: String): TranslationCategory =
            entries.first { it.value == value }
    }
}

object TranslationKeyFactory {
    fun forExercise(canonicalName: String): TranslationKey {
        val normalized = NameNormalizer.normalize(canonicalName)
        return TranslationKey("${TranslationCategory.EXERCISES.value}.$normalized")
    }
}
