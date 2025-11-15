package com.shapyfy.core.domain.exercise

class ExerciseDuplicateException(
    val exercise: Exercise,
    message: String = "Exercise with name '${exercise.name}' already exists"
) : RuntimeException(message)
