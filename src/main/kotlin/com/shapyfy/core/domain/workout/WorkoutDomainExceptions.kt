package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.WorkoutId

class InvalidExerciseReferenceException(
    val exerciseId: ExerciseId,
    message: String = "Exercise with ID $exerciseId does not exist"
) : RuntimeException(message)

/**
 * Thrown when attempting to retrieve a workout that doesn't exist
 */
class WorkoutNotFoundException(
    val workoutId: WorkoutId,
    message: String = "Workout with ID $workoutId not found"
) : RuntimeException(message)
