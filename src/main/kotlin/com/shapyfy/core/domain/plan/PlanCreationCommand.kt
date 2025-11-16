package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.UserId

data class PlanCreationCommand(
    val userId: UserId,
    val name: String,
    val description: String?,
    val days: List<DayCreationData>
) {
    init {
        require(name.isNotBlank()) { "Plan name cannot be blank" }
        require(days.isNotEmpty()) { "Plan must contain at least one day" }
        require(days.size in 1..30) { "Plan must have between 1 and 30 days" }

        val dayIndices = days.map { it.dayIndex }.sorted()
        require(dayIndices == (0 until days.size).toList()) {
            "Day indices must be sequential from 0 to ${days.size - 1}"
        }
    }
}

data class DayCreationData(
    val dayIndex: Int,
    val name: String?,
    val type: DayType,
    val exercises: List<ExerciseCreationData>,
    val notes: String?
) {
    init {
        require(dayIndex >= 0) { "Day index must be non-negative" }
        if (type == DayType.WORKOUT) {
            require(exercises.isNotEmpty()) { "Workout day must contain at least one exercise" }
        }
        if (type == DayType.REST) {
            require(exercises.isEmpty()) { "Rest day cannot contain exercises" }
        }
    }
}

data class ExerciseCreationData(
    val exerciseId: ExerciseId,
    val orderIndex: Int,
    val targetSets: Int,
    val targetReps: Int?,
    val targetWeight: Double?,
    val notes: String?
) {
    init {
        require(orderIndex >= 0) { "Order index must be non-negative" }
        require(targetSets > 0) { "Target sets must be positive" }
        require(targetReps == null || targetReps > 0) { "Target reps must be positive" }
        require(targetWeight == null || targetWeight >= 0) { "Target weight must be non-negative" }
    }
}
