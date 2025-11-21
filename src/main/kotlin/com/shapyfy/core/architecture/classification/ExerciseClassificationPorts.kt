package com.shapyfy.core.architecture.classification

import com.shapyfy.core.domain.exercise.Difficulty
import com.shapyfy.core.domain.exercise.Equipment
import com.shapyfy.core.domain.exercise.MovementPattern
import com.shapyfy.core.domain.exercise.MuscleGroup

/**
 * Result from AI exercise classification service.
 */
data class ExerciseClassification(
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: Set<MuscleGroup>,
    val equipmentRequired: Set<Equipment>,
    val difficulty: Difficulty,
    val movementPattern: MovementPattern
)

interface ExerciseClassificationClient {
    fun classify(exerciseName: String): ExerciseClassification
}
