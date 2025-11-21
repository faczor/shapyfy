package com.shapyfy.core.boundary.exercises

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.shapyfy.core.domain.exercise.ExerciseDetails
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ExerciseResponse(
    val id: UUID,
    val name: String,
    val translationKey: String,
    val primaryMuscleGroup: String,
    val secondaryMuscleGroups: List<String>,
    val equipmentRequired: List<String>,
    val difficulty: String,
    val movementPattern: String
) {
    companion object {
        fun from(details: ExerciseDetails): ExerciseResponse {
            return ExerciseResponse(
                id = details.id.value,
                name = details.localizedName,
                translationKey = details.translationKey.value,
                primaryMuscleGroup = details.primaryMuscleGroup.name,
                secondaryMuscleGroups = details.secondaryMuscleGroups.map { it.name },
                equipmentRequired = details.equipmentRequired.map { it.name },
                difficulty = details.difficulty.name,
                movementPattern = details.movementPattern.name
            )
        }
    }
}

data class GetExercisesResponse(
    val exercises: List<ExerciseResponse>
)

data class ExerciseConflictResponse(
    val id: UUID,
    val message: String,
    val translationKey: String,
    val availableTranslations: Map<String, String>
)
