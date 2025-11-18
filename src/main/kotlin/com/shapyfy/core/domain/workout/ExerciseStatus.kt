package com.shapyfy.core.domain.workout

enum class ExerciseStatus {
    COMPLETED,
    IN_PROGRESS,
    SKIPPED,
    QUEUED;

    fun requiresSets(): Boolean = this == COMPLETED
}
