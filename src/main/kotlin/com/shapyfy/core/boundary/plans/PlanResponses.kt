package com.shapyfy.core.boundary.plans

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.Instant
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserPlansResponse(
    val plans: List<PlanDetailsResponse>
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PlanDetailsResponse(
    val id: String,
    val name: String,
    val description: String?,
    val cycleDays: Int,
    val isActive: Boolean,
    val activationDate: LocalDate?,
    val days: List<PlanDayResponse>,
    val createdAt: Instant,
    val updatedAt: Instant?
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PlanDayResponse(
    val id: String,
    val dayIndex: Int,
    val name: String?,
    val type: String,
    val exercises: List<PlanExerciseResponse>,
    val notes: String?
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PlanExerciseResponse(
    val id: String,
    val exerciseId: String,
    val orderIndex: Int,
    val targetSets: Int,
    val targetReps: Int?,
    val targetWeight: Double?,
    val targetFormatted: String,
    val notes: String?
)
