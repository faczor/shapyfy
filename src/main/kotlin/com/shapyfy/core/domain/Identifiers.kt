package com.shapyfy.core.domain

import java.util.*

sealed interface DomainId {
    val value: UUID
}

@JvmInline
value class UserId(val value: String) {
    companion object {
        fun from(value: String): UserId = UserId(value)
    }

    override fun toString(): String = value
}

@JvmInline
value class WorkoutId(override val value: UUID) : DomainId {
    companion object {
        fun generate(): WorkoutId = WorkoutId(UUID.randomUUID())
        fun from(value: String): WorkoutId = WorkoutId(UUID.fromString(value))
        fun from(value: UUID): WorkoutId = WorkoutId(value)
    }

    override fun toString(): String = value.toString()
}

@JvmInline
value class ExerciseId(override val value: UUID) : DomainId {
    companion object {
        fun generate(): ExerciseId = ExerciseId(UUID.randomUUID())
        fun from(value: String): ExerciseId = ExerciseId(UUID.fromString(value))
        fun from(value: UUID): ExerciseId = ExerciseId(value)
    }

    override fun toString(): String = value.toString()
}

@JvmInline
value class WorkoutExerciseId(override val value: UUID) : DomainId {
    companion object {
        fun generate(): WorkoutExerciseId = WorkoutExerciseId(UUID.randomUUID())
        fun from(value: String): WorkoutExerciseId = WorkoutExerciseId(UUID.fromString(value))
        fun from(value: UUID): WorkoutExerciseId = WorkoutExerciseId(value)
    }

    override fun toString(): String = value.toString()
}

@JvmInline
value class WorkoutSetId(override val value: UUID) : DomainId {
    companion object {
        fun generate(): WorkoutSetId = WorkoutSetId(UUID.randomUUID())
        fun from(value: String): WorkoutSetId = WorkoutSetId(UUID.fromString(value))
        fun from(value: UUID): WorkoutSetId = WorkoutSetId(value)
    }

    override fun toString(): String = value.toString()
}

@JvmInline
value class PlanId(override val value: UUID) : DomainId {
    companion object {
        fun generate(): PlanId = PlanId(UUID.randomUUID())
        fun from(value: String): PlanId = PlanId(UUID.fromString(value))
        fun from(value: UUID): PlanId = PlanId(value)
    }

    override fun toString(): String = value.toString()
}

@JvmInline
value class PlanDayId(override val value: UUID) : DomainId {
    companion object {
        fun generate(): PlanDayId = PlanDayId(UUID.randomUUID())
        fun from(value: String): PlanDayId = PlanDayId(UUID.fromString(value))
        fun from(value: UUID): PlanDayId = PlanDayId(value)
    }

    override fun toString(): String = value.toString()
}

@JvmInline
value class PlanExerciseId(override val value: UUID) : DomainId {
    companion object {
        fun generate(): PlanExerciseId = PlanExerciseId(UUID.randomUUID())
        fun from(value: String): PlanExerciseId = PlanExerciseId(UUID.fromString(value))
        fun from(value: UUID): PlanExerciseId = PlanExerciseId(value)
    }

    override fun toString(): String = value.toString()
}
