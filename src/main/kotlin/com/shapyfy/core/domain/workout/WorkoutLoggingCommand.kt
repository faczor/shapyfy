package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.PlanDayId
import com.shapyfy.core.domain.UserId
import java.time.Instant

/**
 * Command to log a workout
 *
 * Can be:
 * - Freestyle: planDayId is null
 * - Planned: planDayId references the plan day this workout is based on
 */
data class WorkoutLoggingCommand(
    val userId: UserId,
    val startTime: Instant,
    val endTime: Instant,
    val exercises: List<ExerciseLoggingData>,
    val planDayId: PlanDayId? = null  // null = freestyle workout
) {
    init {
        require(exercises.isNotEmpty()) { "Workout must contain at least one exercise" }
        require(endTime.isAfter(startTime)) { "End time must be after start time" }
    }
}

data class ExerciseLoggingData(
    val exerciseId: ExerciseId,
    val orderIndex: Int,
    val status: ExerciseStatus,
    val sets: List<SetLoggingData>
) {
    init {
        require(orderIndex >= 0) { "Order index must be non-negative" }
        if (status.requiresSets()) {
            require(sets.isNotEmpty()) { "Exercise must contain at least one set when status is $status" }
        }
    }
}

data class SetLoggingData(
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val timestamp: Instant
) {
    init {
        require(setNumber > 0) { "Set number must be positive" }
        require(weight >= 0) { "Weight must be non-negative" }
        require(reps > 0) { "Reps must be positive" }
    }
}
