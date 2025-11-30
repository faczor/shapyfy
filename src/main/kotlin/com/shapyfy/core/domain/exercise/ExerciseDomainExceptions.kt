package com.shapyfy.core.domain.exercise

class ExerciseDuplicateException(
    val exercise: Exercise,
    message: String = "Exercise with name '${exercise.canonicalName.value}' already exists"
) : RuntimeException(message)
