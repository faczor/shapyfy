package com.shapyfy.core.boundary.plans

import java.time.Instant
import java.time.LocalDate

data class UserPlansResponse(
    val plans: List<PlanDetailsResponse>
)

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

data class PlanDayResponse(
    val id: String,
    val dayIndex: Int,
    val name: String?,
    val type: String,
    val exercises: List<PlanExerciseResponse>,
    val notes: String?
)

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
