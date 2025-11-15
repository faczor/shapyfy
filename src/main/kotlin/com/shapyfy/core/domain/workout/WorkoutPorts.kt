package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.WorkoutId

/**
 * Port for workout persistence operations.
 * Defines the contract that must be implemented by the persistence adapter.
 */
interface WorkoutRepository {
    /**
     * Saves a new workout to the repository
     */
    fun save(workout: Workout): Workout

    /**
     * Finds a workout by its ID
     * @return the workout if found, null otherwise
     */
    fun findById(id: WorkoutId): Workout?

    fun findAllByUserId(userId: UserId): List<Workout>
}

/**
 * Port for recording workout-related metrics.
 * Allows the domain to emit business events without depending on infrastructure.
 */
interface WorkoutMetricsPort {
    /**
     * Records that a workout was successfully logged
     */
    fun recordWorkoutLogged(userId: UserId, totalSets: Int, totalVolume: Double)

    /**
     * Records that an exercise reference was invalid
     */
    fun recordInvalidExerciseReference(exerciseId: ExerciseId)
}
