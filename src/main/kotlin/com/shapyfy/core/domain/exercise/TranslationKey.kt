package com.shapyfy.core.domain.exercise

@JvmInline
value class TranslationKey(val value: String) {
    init {
        require(value.isNotBlank()) { "Translation key cannot be blank" }
    }
}

object TranslationKeyFactory {
    private const val EXERCISE_PREFIX = "exercises"

    fun forExercise(canonicalName: String): TranslationKey {
        return TranslationKey("$EXERCISE_PREFIX.$canonicalName")
    }

    fun forExercise(canonicalName: CanonicalExerciseName): TranslationKey {
        return TranslationKey("$EXERCISE_PREFIX.${canonicalName.value}")
    }
}
