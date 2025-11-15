package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.WorkoutId
import java.time.Instant

/**
 * Core domain entity representing a completed workout session.
 * A workout is an aggregate root containing exercises and their sets.
 */
data class Workout(
    val id: WorkoutId,
    val userId: UserId,
    val status: WorkoutStatus,
    val startTime: Instant,
    val endTime: Instant,
    val exercises: List<WorkoutExercise>,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    /**
     * Calculates total number of sets across all exercises
     */
    fun totalSets(): Int = exercises.sumOf { it.sets.size }

    /**
     * Calculates total volume (weight * reps) across all sets
     */
    fun totalVolume(): Double = exercises.sumOf { exercise ->
        exercise.sets.sumOf { set -> set.weight * set.reps }
    }

    companion object {
        fun new(
            userId: UserId,
            startTime: Instant,
            endTime: Instant,
            exercises: List<WorkoutExercise>
        ): Workout {
            require(exercises.isNotEmpty()) { "Workout must contain at least one exercise" }
            require(endTime.isAfter(startTime)) { "End time must be after start time" }

            return Workout(
                id = WorkoutId.Companion.generate(),
                userId = userId,
                status = WorkoutStatus.COMPLETED,
                startTime = startTime,
                endTime = endTime,
                exercises = exercises,
                createdAt = Instant.now(),
                updatedAt = null
            )
        }
    }
}
