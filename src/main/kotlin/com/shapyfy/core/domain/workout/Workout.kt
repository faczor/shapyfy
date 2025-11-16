package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.PlanDayId
import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.WorkoutId
import java.time.Instant

/**
 * Core domain entity representing a completed workout session.
 * A workout is an aggregate root containing exercises and their sets.
 *
 * Workouts can be:
 * - Planned: Started from a specific plan day (planDayId != null)
 * - Freestyle: Ad-hoc workout not based on any plan (planDayId == null)
 */
data class Workout(
    val id: WorkoutId,
    val userId: UserId,
    val status: WorkoutStatus,
    val startTime: Instant,
    val endTime: Instant,
    val exercises: List<WorkoutExercise>,
    val planDayId: PlanDayId?,  // null = freestyle workout, not null = started from plan
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

    /**
     * Whether this workout was started from a plan
     */
    fun isPlannedWorkout(): Boolean = planDayId != null

    /**
     * Whether this is a freestyle workout (not based on a plan)
     */
    fun isFreestyleWorkout(): Boolean = planDayId == null

    companion object {
        /**
         * Create a new freestyle workout (not based on a plan)
         */
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
                planDayId = null,
                createdAt = Instant.now(),
                updatedAt = null
            )
        }

        /**
         * Create a new planned workout (based on a plan day)
         */
        fun fromPlan(
            userId: UserId,
            planDayId: PlanDayId,
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
                planDayId = planDayId,
                createdAt = Instant.now(),
                updatedAt = null
            )
        }
    }
}
