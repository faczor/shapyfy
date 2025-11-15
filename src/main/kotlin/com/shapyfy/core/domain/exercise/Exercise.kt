package com.shapyfy.core.domain.exercise

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language
import java.time.Instant

data class Exercise(
    val id: ExerciseId,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    companion object {
        fun new(name: String): Exercise {
            require(name.isNotBlank()) { "Exercise name cannot be blank" }
            return Exercise(
                id = ExerciseId.Companion.generate(),
                name = name,
                createdAt = Instant.now(),
                updatedAt = null
            )
        }
    }
}

data class ExerciseCreationCommand(
    val rawName: String,
    val preferredLanguage: Language
) {
    init {
        require(rawName.isNotBlank()) { "Exercise name cannot be blank" }
    }
}

data class ExerciseCreationResult(
    val id: ExerciseId,
    val localizedName: String,
    val translationKey: TranslationKey
)

data class ExerciseSummary(
    val id: ExerciseId,
    val localizedName: String,
    val translationKey: TranslationKey
)
