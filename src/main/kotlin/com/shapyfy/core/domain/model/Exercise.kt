package com.shapyfy.core.domain.model

import java.time.Instant
import java.util.UUID

/**
 * Core domain entity representing an exercise.
 * Name is the canonical identifier (e.g., "squat", "bench_press")
 */
data class Exercise(
    val id: UUID,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    companion object {
        fun new(name: String): Exercise {
            require(name.isNotBlank()) { "Exercise name cannot be blank" }
            return Exercise(
                id = UUID.randomUUID(),
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
    val id: UUID,
    val localizedName: String,
    val translationKey: TranslationKey
)

data class ExerciseSummary(
    val id: UUID,
    val localizedName: String,
    val translationKey: TranslationKey
)
