package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.exercise.ExerciseRepository
import org.springframework.stereotype.Service

/**
 * Domain service responsible for logging workout sessions.
 * Validates exercise references and delegates to the repository.
 */
@Service
class WorkoutLogger(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val metricsPort: WorkoutMetricsPort
) {
    /**
     * Logs a completed workout session.
     * Validates that all referenced exercises exist before persisting.
     *
     * @param command the workout logging command
     * @return the persisted workout
     * @throws InvalidExerciseReferenceException if any exercise ID doesn't exist
     */
    fun logWorkout(command: WorkoutLoggingCommand): Workout {
        // Validate all exercise references exist
        val invalidExerciseIds = command.exercises
            .map { it.exerciseId }
            .filter { !exerciseRepository.existsById(it) }

        if (invalidExerciseIds.isNotEmpty()) {
            invalidExerciseIds.forEach { metricsPort.recordInvalidExerciseReference(it) }
            throw InvalidExerciseReferenceException(invalidExerciseIds.first())
        }

        // Build domain model from command
        val workoutExercises = command.exercises.map { exerciseData ->
            val sets = exerciseData.sets.map { setData ->
                WorkoutSet.new(
                    setNumber = setData.setNumber,
                    weight = setData.weight,
                    reps = setData.reps,
                    timestamp = setData.timestamp
                )
            }

            WorkoutExercise.new(
                exerciseId = exerciseData.exerciseId,
                orderIndex = exerciseData.orderIndex,
                status = exerciseData.status,
                sets = sets
            )
        }

        // Create workout based on whether it's planned or freestyle
        val workout = if (command.planDayId != null) {
            Workout.fromPlan(
                userId = command.userId,
                planDayId = command.planDayId,
                startTime = command.startTime,
                endTime = command.endTime,
                exercises = workoutExercises
            )
        } else {
            Workout.new(
                userId = command.userId,
                startTime = command.startTime,
                endTime = command.endTime,
                exercises = workoutExercises
            )
        }

        // Persist workout
        val savedWorkout = workoutRepository.save(workout)

        // Record metrics
        metricsPort.recordWorkoutLogged(
            userId = savedWorkout.userId,
            totalSets = savedWorkout.totalSets(),
            totalVolume = savedWorkout.totalVolume()
        )

        return savedWorkout
    }
}
