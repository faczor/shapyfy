package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.UserId
import java.time.Instant

data class WorkoutLoggingCommand(
    val userId: UserId,
    val startTime: Instant,
    val endTime: Instant,
    val exercises: List<ExerciseLoggingData>
) {
    init {
        require(exercises.isNotEmpty()) { "Workout must contain at least one exercise" }
        require(endTime.isAfter(startTime)) { "End time must be after start time" }
    }
}

data class ExerciseLoggingData(
    val exerciseId: ExerciseId,
    val orderIndex: Int,
    val sets: List<SetLoggingData>
) {
    init {
        require(sets.isNotEmpty()) { "Exercise must contain at least one set" }
        require(orderIndex >= 0) { "Order index must be non-negative" }
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
