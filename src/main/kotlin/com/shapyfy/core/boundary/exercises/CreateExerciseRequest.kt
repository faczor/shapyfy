package com.shapyfy.core.boundary.exercises

import jakarta.validation.constraints.NotBlank

data class CreateExerciseRequest(

    @field:NotBlank
    val name: String
)
