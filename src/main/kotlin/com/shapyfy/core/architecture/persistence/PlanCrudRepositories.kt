package com.shapyfy.core.architecture.persistence

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlanCrudRepository : CrudRepository<PlanEntity, UUID> {
    fun findAllByUserId(userId: String): List<PlanEntity>
    fun findByUserIdAndIsActive(userId: String, isActive: Boolean): PlanEntity?

    @Query("SELECT * FROM workout_plans WHERE user_id IS NULL")
    fun findAllTemplates(): List<PlanEntity>
}

@Repository
interface PlanDayCrudRepository : CrudRepository<PlanDayEntity, UUID> {
    fun findByPlanId(planId: UUID): List<PlanDayEntity>
    fun findByPlanIdIn(planIds: List<UUID>): List<PlanDayEntity>
}

@Repository
interface PlanExerciseCrudRepository : CrudRepository<PlanExerciseEntity, UUID> {
    fun findByPlanDayId(planDayId: UUID): List<PlanExerciseEntity>
    fun findByPlanDayIdIn(planDayIds: List<UUID>): List<PlanExerciseEntity>
}
