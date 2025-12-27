package com.shapyfy.core.boundary.plans

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreatePlanRequest(
    @field:NotBlank(message = "Plan name is required")
    val name: String,

    val description: String?,

    @field:NotEmpty(message = "Plan must contain at least one day")
    @field:Valid
    val days: List<CreatePlanDayRequest>
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreatePlanDayRequest(
    @field:Min(0, message = "Day index must be non-negative")
    @field:Max(29, message = "Day index must be less than 30")
    val dayIndex: Int,

    val name: String?,

    @field:NotBlank(message = "Day type is required")
    val type: String,

    @field:Valid
    val exercises: List<CreatePlanExerciseRequest> = emptyList(),

    val notes: String?
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreatePlanExerciseRequest(
    @field:NotBlank(message = "Exercise ID is required")
    val exerciseId: String,

    @field:Min(0, message = "Order index must be non-negative")
    val orderIndex: Int,

    @field:NotEmpty(message = "Exercise must have at least one set")
    @field:Valid
    val sets: List<CreateSetRequest>,

    val notes: String?
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateSetRequest(
    @field:Positive(message = "Reps must be positive if specified")
    val reps: Int?,

    @field:Min(0, message = "Weight must be non-negative if specified")
    val weight: Double?
)
