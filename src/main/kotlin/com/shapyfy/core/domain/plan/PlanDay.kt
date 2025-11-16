package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.PlanDayId

data class PlanDay(
    val id: PlanDayId,
    val dayIndex: Int,
    val name: String?,
    val type: DayType,
    val exercises: List<PlanExercise>,
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

    fun totalSets(): Int = exercises.sumOf { it.targetSets }

    fun isWorkoutDay(): Boolean = type == DayType.WORKOUT

    fun isRestDay(): Boolean = type == DayType.REST

    companion object {
        fun workout(
            dayIndex: Int,
            name: String?,
            exercises: List<PlanExercise>,
            notes: String? = null
        ): PlanDay {
            return PlanDay(
                id = PlanDayId.generate(),
                dayIndex = dayIndex,
                name = name,
                type = DayType.WORKOUT,
                exercises = exercises,
                notes = notes
            )
        }

        fun rest(
            dayIndex: Int,
            name: String? = "Rest",
            notes: String? = null
        ): PlanDay {
            return PlanDay(
                id = PlanDayId.generate(),
                dayIndex = dayIndex,
                name = name,
                type = DayType.REST,
                exercises = emptyList(),
                notes = notes
            )
        }
    }
}
