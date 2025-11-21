package com.shapyfy.core.architecture.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Table("workout_plans")
class PlanEntity(
    @Id
    @Column("id")
    private val _id: UUID,

    @Column("user_id")
    val userId: String?,  // null = global template

    @Column("name")
    val name: String,

    @Column("description")
    val description: String?,

    @Column("cycle_days")
    val cycleDays: Int,

    @Column("is_active")
    val isActive: Boolean,

    @Column("activation_date")
    val activationDate: LocalDate?,

    @Column("is_recommended")
    val isRecommended: Boolean,

    @Column("recommendation_score")
    val recommendationScore: Double?,

    @Column("target_experience_level")
    val targetExperienceLevel: String?,

    @Column("target_goal")
    val targetGoal: String?,

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
            userId: String?,
            name: String,
            description: String?,
            cycleDays: Int,
            isActive: Boolean,
            activationDate: LocalDate?,
            isRecommended: Boolean,
            recommendationScore: Double?,
            targetExperienceLevel: String?,
            targetGoal: String?,
            createdAt: Instant,
            updatedAt: Instant?
        ): PlanEntity = PlanEntity(
            _id = id,
            userId = userId,
            name = name,
            description = description,
            cycleDays = cycleDays,
            isActive = isActive,
            activationDate = activationDate,
            isRecommended = isRecommended,
            recommendationScore = recommendationScore,
            targetExperienceLevel = targetExperienceLevel,
            targetGoal = targetGoal,
            createdAt = createdAt,
            updatedAt = updatedAt
        ).apply {
            isNewEntity = true
        }
    }
}

@Table("plan_days")
class PlanDayEntity(
    @Id
    @Column("id")
    private val _id: UUID,

    @Column("plan_id")
    val planId: UUID,

    @Column("day_index")
    val dayIndex: Int,

    @Column("name")
    val name: String?,

    @Column("day_type")
    val dayType: String,

    @Column("notes")
    val notes: String?
) : Persistable<UUID> {

    @Transient
    private var isNewEntity: Boolean = false

    override fun getId(): UUID = _id
    override fun isNew(): Boolean = isNewEntity

    companion object {
        fun new(
            id: UUID,
            planId: UUID,
            dayIndex: Int,
            name: String?,
            dayType: String,
            notes: String?
        ): PlanDayEntity = PlanDayEntity(
            _id = id,
            planId = planId,
            dayIndex = dayIndex,
            name = name,
            dayType = dayType,
            notes = notes
        ).apply {
            isNewEntity = true
        }
    }
}

@Table("plan_exercises")
class PlanExerciseEntity(
    @Id
    @Column("id")
    private val _id: UUID,

    @Column("plan_day_id")
    val planDayId: UUID,

    @Column("exercise_id")
    val exerciseId: UUID,

    @Column("order_index")
    val orderIndex: Int,

    @Column("target_sets")
    val targetSets: Int,

    @Column("target_reps")
    val targetReps: Int?,

    @Column("target_weight")
    val targetWeight: Double?,

    @Column("notes")
    val notes: String?
) : Persistable<UUID> {

    @Transient
    private var isNewEntity: Boolean = false

    override fun getId(): UUID = _id
    override fun isNew(): Boolean = isNewEntity

    companion object {
        fun new(
            id: UUID,
            planDayId: UUID,
            exerciseId: UUID,
            orderIndex: Int,
            targetSets: Int,
            targetReps: Int?,
            targetWeight: Double?,
            notes: String?
        ): PlanExerciseEntity = PlanExerciseEntity(
            _id = id,
            planDayId = planDayId,
            exerciseId = exerciseId,
            orderIndex = orderIndex,
            targetSets = targetSets,
            targetReps = targetReps,
            targetWeight = targetWeight,
            notes = notes
        ).apply {
            isNewEntity = true
        }
    }
}
