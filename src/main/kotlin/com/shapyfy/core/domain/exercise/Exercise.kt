package com.shapyfy.core.domain.exercise

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language
import java.time.Instant

/**
 * Canonical exercise name - normalized, lowercase identifier with underscores.
 * Examples: "squats", "bench_press", "deadlift"
 */
@JvmInline
value class CanonicalExerciseName(val value: String) {
    init {
        require(value.isNotBlank()) { "Canonical exercise name cannot be blank" }
        require(value == value.lowercase()) { "Canonical exercise name must be lowercase" }
    }

    override fun toString(): String = value
}

data class Exercise(
    val id: ExerciseId,
    val canonicalName: CanonicalExerciseName,
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: Set<MuscleGroup>,
    val equipmentRequired: Set<Equipment>,
    val difficulty: Difficulty,
    val movementPattern: MovementPattern,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    init {
        require(equipmentRequired.isNotEmpty()) { "Exercise must specify equipment (use BODYWEIGHT if none)" }
        require(!secondaryMuscleGroups.contains(primaryMuscleGroup)) { "Primary muscle group cannot be in secondary muscle groups" }
    }

    companion object {
        fun new(
            canonicalName: CanonicalExerciseName,
            primaryMuscleGroup: MuscleGroup,
            secondaryMuscleGroups: Set<MuscleGroup>,
            equipmentRequired: Set<Equipment>,
            difficulty: Difficulty,
            movementPattern: MovementPattern
        ): Exercise {
            return Exercise(
                id = ExerciseId.Companion.generate(),
                canonicalName = canonicalName,
                primaryMuscleGroup = primaryMuscleGroup,
                secondaryMuscleGroups = secondaryMuscleGroups,
                equipmentRequired = equipmentRequired,
                difficulty = difficulty,
                movementPattern = movementPattern,
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

data class ExerciseDetails(
    val id: ExerciseId,
    val localizedName: String,
    val translationKey: TranslationKey,
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: Set<MuscleGroup>,
    val equipmentRequired: Set<Equipment>,
    val difficulty: Difficulty,
    val movementPattern: MovementPattern
)
