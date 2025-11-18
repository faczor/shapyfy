package com.shapyfy.core.architecture.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("workouts")
class WorkoutEntity(
    @Id
    @Column("id")
    private val _id: UUID,

    @Column("user_id")
    val userId: String,  // Firebase UID

    @Column("status")
    val status: String,

    @Column("start_time")
    val startTime: Instant,

    @Column("end_time")
    val endTime: Instant,

    @Column("plan_day_id")
    val planDayId: UUID?,  // null = freestyle workout

    @Column("created_at")
    val createdAt: Instant,

    @Column("updated_at")
    val updatedAt: Instant?
) : Persistable<UUID> {

    @Transient
    private var isNewEntity: Boolean = false

    override fun getId(): UUID = _id
    override fun isNew(): Boolean = isNewEntity

    companion object {
        fun new(
            id: UUID,
            userId: String,
            status: String,
            startTime: Instant,
            endTime: Instant,
            planDayId: UUID?,
            createdAt: Instant,
            updatedAt: Instant?
        ): WorkoutEntity = WorkoutEntity(
            _id = id,
            userId = userId,
            status = status,
            startTime = startTime,
            endTime = endTime,
            planDayId = planDayId,
            createdAt = createdAt,
            updatedAt = updatedAt
        ).apply {
            isNewEntity = true
        }
    }
}

@Table("workout_exercises")
class WorkoutExerciseEntity(
    @Id
    @Column("id")
    private val _id: UUID,

    @Column("workout_id")
    val workoutId: UUID,

    @Column("exercise_id")
    val exerciseId: UUID,

    @Column("order_index")
    val orderIndex: Int,

    @Column("status")
    val status: String
) : Persistable<UUID> {

    @Transient
    private var isNewEntity: Boolean = false

    override fun getId(): UUID = _id
    override fun isNew(): Boolean = isNewEntity

    companion object {
        fun new(
            id: UUID,
            workoutId: UUID,
            exerciseId: UUID,
            orderIndex: Int,
            status: String
        ): WorkoutExerciseEntity = WorkoutExerciseEntity(
            _id = id,
            workoutId = workoutId,
            exerciseId = exerciseId,
            orderIndex = orderIndex,
            status = status
        ).apply {
            isNewEntity = true
        }
    }
}

@Table("workout_sets")
class WorkoutSetEntity(
    @Id
    @Column("id")
    private val _id: UUID,

    @Column("workout_exercise_id")
    val workoutExerciseId: UUID,

    @Column("set_number")
    val setNumber: Int,

    @Column("weight")
    val weight: Double,

    @Column("reps")
    val reps: Int,

    @Column("timestamp")
    val timestamp: Instant
) : Persistable<UUID> {

    @Transient
    private var isNewEntity: Boolean = false

    override fun getId(): UUID = _id
    override fun isNew(): Boolean = isNewEntity

    companion object {
        fun new(
            id: UUID,
            workoutExerciseId: UUID,
            setNumber: Int,
            weight: Double,
            reps: Int,
            timestamp: Instant
        ): WorkoutSetEntity = WorkoutSetEntity(
            _id = id,
            workoutExerciseId = workoutExerciseId,
            setNumber = setNumber,
            weight = weight,
            reps = reps,
            timestamp = timestamp
        ).apply {
            isNewEntity = true
        }
    }
}
