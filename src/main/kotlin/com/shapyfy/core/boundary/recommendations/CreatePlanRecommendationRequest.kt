package com.shapyfy.core.boundary.recommendations

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.shapyfy.core.domain.exercise.Equipment
import com.shapyfy.core.domain.plan.ExperienceLevel
import com.shapyfy.core.domain.plan.FitnessGoal
import com.shapyfy.core.domain.plan.WorkoutLocation
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreatePlanRecommendationRequest(
    @field:NotNull(message = "Goal is required")
    val goal: FitnessGoal,

    @field:NotNull(message = "Experience level is required")
    val experience: ExperienceLevel,

    @field:NotNull(message = "Workout location is required")
    val location: WorkoutLocation,

    @field:NotNull(message = "Frequency is required")
    @field:Positive(message = "Frequency must be greater than 0")
    val frequency: Int,

    val availableAccessories: List<Equipment>?
)
