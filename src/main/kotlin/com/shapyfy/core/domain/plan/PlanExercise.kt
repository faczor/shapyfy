package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.PlanExerciseId

data class PlanExercise(
    val id: PlanExerciseId,
    val exerciseId: ExerciseId,
    val orderIndex: Int,
    val sets: List<ExerciseSet>,
    val notes: String?
) {
    init {
        require(orderIndex >= 0) { "Order index must be non-negative" }
        require(sets.isNotEmpty()) { "Exercise must have at least one set" }
    }

    fun totalSets(): Int = sets.size

    fun formatTarget(): String = buildString {
        if (sets.isEmpty()) return ""

        if (sets.all { it.reps == sets.first().reps && it.weight == sets.first().weight }) {
            append("${sets.size}x${sets.first().format()}")
        } else {
            append(sets.joinToString(", ") { it.format() })
        }
    }

    companion object {
        fun new(
            exerciseId: ExerciseId,
            orderIndex: Int,
            sets: List<ExerciseSet>,
            notes: String? = null
        ): PlanExercise {
            return PlanExercise(
                id = PlanExerciseId.generate(),
                exerciseId = exerciseId,
                orderIndex = orderIndex,
                sets = sets,
                notes = notes
            )
        }

        fun withUniformSets(
            exerciseId: ExerciseId,
            orderIndex: Int,
            numberOfSets: Int,
            repsPerSet: Int,
            weightPerSet: Double? = null,
            notes: String? = null
        ): PlanExercise {
            val sets = List(numberOfSets) { ExerciseSet(repsPerSet, weightPerSet) }
            return new(exerciseId, orderIndex, sets, notes)
        }
    }
}
