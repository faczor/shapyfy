package com.shapyfy.core.domain.plan

data class ExerciseSet(
    val reps: Int?,
    val weight: Double?
) {
    init {
        require(reps == null || reps > 0) { "Reps must be positive if specified" }
        require(weight == null || weight >= 0) { "Weight must be non-negative if specified" }
    }

    fun format(): String = buildString {
        append(reps?.toString() ?: "?")
        if (weight != null) {
            append(" @ ${weight}kg")
        }
    }

    companion object {
        fun withReps(reps: Int): ExerciseSet = ExerciseSet(reps, null)

        fun withRepsAndWeight(reps: Int, weight: Double): ExerciseSet = ExerciseSet(reps, weight)

        fun amrap(): ExerciseSet = ExerciseSet(null, null)
    }
}
