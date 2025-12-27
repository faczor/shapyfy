package com.shapyfy.core.boundary.plans

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.shapyfy.core.domain.plan.ExerciseSet
import com.shapyfy.core.domain.plan.PlanDay
import com.shapyfy.core.domain.plan.PlanExercise
import com.shapyfy.core.domain.plan.WorkoutPlan
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
    val sets: List<ExerciseSetResponse>,
    val targetFormatted: String,
    val notes: String?
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ExerciseSetResponse(
    val reps: Int?,
    val weight: Double?
)

// Extension functions to convert domain models to responses
fun WorkoutPlan.toDetailsResponse(): PlanDetailsResponse =
    PlanDetailsResponse(
        id = id.toString(),
        name = name,
        description = description,
        cycleDays = cycleDays,
        isActive = isActive,
        activationDate = activationDate,
        days = days.map { it.toResponse() },
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun PlanDay.toResponse(): PlanDayResponse =
    PlanDayResponse(
        id = id.toString(),
        dayIndex = dayIndex,
        name = name,
        type = type.name,
        exercises = exercises.map { it.toResponse() },
        notes = notes
    )

fun PlanExercise.toResponse(): PlanExerciseResponse =
    PlanExerciseResponse(
        id = id.toString(),
        exerciseId = exerciseId.toString(),
        orderIndex = orderIndex,
        sets = sets.map { it.toResponse() },
        targetFormatted = formatTarget(),
        notes = notes
    )

fun ExerciseSet.toResponse(): ExerciseSetResponse =
    ExerciseSetResponse(
        reps = reps,
        weight = weight
    )
