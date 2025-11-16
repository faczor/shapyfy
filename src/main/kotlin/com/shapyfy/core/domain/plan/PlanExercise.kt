package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.PlanExerciseId

data class PlanExercise(
    val id: PlanExerciseId,
    val exerciseId: ExerciseId,
    val orderIndex: Int,
    val targetSets: Int,
    val targetReps: Int?,
    val targetWeight: Double?,
    val notes: String?
) {
    init {
        require(orderIndex >= 0) { "Order index must be non-negative" }
        require(targetSets > 0) { "Target sets must be positive" }
        require(targetReps == null || targetReps > 0) { "Target reps must be positive" }
        require(targetWeight == null || targetWeight >= 0) { "Target weight must be non-negative" }
    }

    fun formatTarget(): String = buildString {
        append("${targetSets}x${targetReps ?: "?"}")
        if (targetWeight != null) {
            append(" @ ${targetWeight}kg")
        }
    }

    companion object {
        fun new(
            exerciseId: ExerciseId,
            orderIndex: Int,
            targetSets: Int,
            targetReps: Int? = null,
            targetWeight: Double? = null,
            notes: String? = null
        ): PlanExercise {
            return PlanExercise(
                id = PlanExerciseId.generate(),
                exerciseId = exerciseId,
                orderIndex = orderIndex,
                targetSets = targetSets,
                targetReps = targetReps,
                targetWeight = targetWeight,
                notes = notes
            )
        }
    }
}
