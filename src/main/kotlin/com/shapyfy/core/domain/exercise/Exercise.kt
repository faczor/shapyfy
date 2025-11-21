package com.shapyfy.core.domain.exercise

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language
import java.time.Instant

data class Exercise(
    val id: ExerciseId,
    val name: String,
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: Set<MuscleGroup>,
    val equipmentRequired: Set<Equipment>,
    val difficulty: Difficulty,
    val movementPattern: MovementPattern,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    init {
        require(name.isNotBlank()) { "Exercise name cannot be blank" }
        require(equipmentRequired.isNotEmpty()) { "Exercise must specify equipment (use BODYWEIGHT if none)" }
        require(!secondaryMuscleGroups.contains(primaryMuscleGroup)) { "Primary muscle group cannot be in secondary muscle groups" }
    }

    companion object {
        fun new(
            name: String,
            primaryMuscleGroup: MuscleGroup,
            secondaryMuscleGroups: Set<MuscleGroup>,
            equipmentRequired: Set<Equipment>,
            difficulty: Difficulty,
            movementPattern: MovementPattern
        ): Exercise {
            return Exercise(
                id = ExerciseId.Companion.generate(),
                name = name,
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
