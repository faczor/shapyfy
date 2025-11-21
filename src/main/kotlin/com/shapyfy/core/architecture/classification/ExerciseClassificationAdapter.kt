package com.shapyfy.core.architecture.classification

import com.shapyfy.core.domain.exercise.ExerciseClassificationPort
import com.shapyfy.core.domain.exercise.ExerciseProperties
import org.springframework.stereotype.Component

/**
 * Adapter that implements the domain port using the architecture-layer AI client.
 * Bridges the gap between domain and infrastructure concerns.
 */
@Component
class ExerciseClassificationAdapter(
    private val classificationClient: ExerciseClassificationClient
) : ExerciseClassificationPort {

    override fun classify(exerciseName: String): ExerciseProperties {
        val classification = classificationClient.classify(exerciseName)

        return ExerciseProperties(
            primaryMuscleGroup = classification.primaryMuscleGroup,
            secondaryMuscleGroups = classification.secondaryMuscleGroups,
            equipmentRequired = classification.equipmentRequired,
            difficulty = classification.difficulty,
            movementPattern = classification.movementPattern
        )
    }
}
