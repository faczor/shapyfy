package com.shapyfy.core.domain

import com.shapyfy.core.domain.model.Exercise

class ExerciseDuplicateException(
    val exercise: Exercise,
    message: String = "Exercise with name '${exercise.name}' already exists"
) : RuntimeException(message)
