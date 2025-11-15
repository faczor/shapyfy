package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.WorkoutSetId
import java.time.Instant

/**
 * Represents a single set within an exercise
 */
data class WorkoutSet(
    val id: WorkoutSetId,
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val timestamp: Instant
) {
    init {
        require(setNumber > 0) { "Set number must be positive" }
        require(weight >= 0) { "Weight must be non-negative" }
        require(reps > 0) { "Reps must be positive" }
    }

    companion object {
        fun new(
            setNumber: Int,
            weight: Double,
            reps: Int,
            timestamp: Instant
        ): WorkoutSet {
            return WorkoutSet(
                id = WorkoutSetId.Companion.generate(),
                setNumber = setNumber,
                weight = weight,
                reps = reps,
                timestamp = timestamp
            )
        }
    }
}
