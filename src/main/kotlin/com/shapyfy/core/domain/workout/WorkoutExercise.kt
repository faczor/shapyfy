package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.WorkoutExerciseId

/**
 * Represents an exercise performed within a workout
 */
data class WorkoutExercise(
    val id: WorkoutExerciseId,
    val exerciseId: ExerciseId,
    val orderIndex: Int,
    val status: ExerciseStatus,
    val sets: List<WorkoutSet>
) {
    init {
        require(orderIndex >= 0) { "Order index must be non-negative" }
        if (status.requiresSets()) {
            require(sets.isNotEmpty()) { "Exercise must contain at least one set when status is $status" }
        }
    }

    companion object {
        fun new(
            exerciseId: ExerciseId,
            orderIndex: Int,
            status: ExerciseStatus,
            sets: List<WorkoutSet>
        ): WorkoutExercise {
            return WorkoutExercise(
                id = WorkoutExerciseId.Companion.generate(),
                exerciseId = exerciseId,
                orderIndex = orderIndex,
                status = status,
                sets = sets
            )
        }
    }
}
