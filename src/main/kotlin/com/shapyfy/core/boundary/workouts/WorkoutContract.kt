package com.shapyfy.core.boundary.workouts

import com.fasterxml.jackson.annotation.JsonProperty
import com.shapyfy.core.boundary.exercises.ValidExerciseSets
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.UUID

/**
 * Request to log a workout session
 */
data class LogWorkoutRequest(
    @field:NotNull(message = "start_time is required")
    @JsonProperty("start_time")
    val startTime: Long, // epoch millis

    @field:NotNull(message = "end_time is required")
    @JsonProperty("end_time")
    val endTime: Long, // epoch millis

    @field:NotEmpty(message = "exercises cannot be empty")
    @field:Valid
    val exercises: List<WorkoutExerciseRequest>
)

@ValidExerciseSets
data class WorkoutExerciseRequest(
    @field:NotNull(message = "exercise_id is required")
    @JsonProperty("exercise_id")
    val exerciseId: UUID,

    @field:NotNull(message = "order_index is required")
    @field:Min(value = 0, message = "order_index must be non-negative")
    @JsonProperty("order_index")
    val orderIndex: Int,

    @field:NotBlank(message = "status is required")
    val status: String,

    @field:Valid
    val sets: List<WorkoutSetRequest> = emptyList()
)

data class WorkoutSetRequest(
    @field:NotNull(message = "set_number is required")
    @field:Min(value = 1, message = "set_number must be positive")
    @JsonProperty("set_number")
    val setNumber: Int,

    @field:NotNull(message = "weight is required")
    @field:Min(value = 0, message = "weight must be non-negative")
    val weight: Double,

    @field:NotNull(message = "reps is required")
    @field:Min(value = 1, message = "reps must be positive")
    val reps: Int,

    @field:NotNull(message = "timestamp is required")
    val timestamp: Long // epoch millis
)

/**
 * Response for a logged workout
 */
data class WorkoutResponse(
    val id: UUID,

    @JsonProperty("user_id")
    val userId: String,  // Firebase UID

    val status: String,

    @JsonProperty("start_time")
    val startTime: Long, // epoch millis

    @JsonProperty("end_time")
    val endTime: Long, // epoch millis

    @JsonProperty("total_sets")
    val totalSets: Int,

    @JsonProperty("total_volume")
    val totalVolume: Double,

    val exercises: List<WorkoutExerciseResponse>
)

data class WorkoutExerciseResponse(
    val id: UUID,

    @JsonProperty("exercise_id")
    val exerciseId: UUID,

    @JsonProperty("order_index")
    val orderIndex: Int,

    val status: String,

    val sets: List<WorkoutSetResponse>
)

data class WorkoutSetResponse(
    val id: UUID,
    @JsonProperty("set_number")

    val setNumber: Int,

    val weight: Double,

    val reps: Int,

    val timestamp: Long // epoch millis
)

/**
 * List of workouts
 */
data class WorkoutListResponse(
    val workouts: List<WorkoutResponse>
)
