package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.PlanId
import com.shapyfy.core.domain.UserId

interface PlanRepository {
    fun save(plan: WorkoutPlan): WorkoutPlan
    fun findById(id: PlanId): WorkoutPlan?
    fun findAllByUserId(userId: UserId): List<WorkoutPlan>
    fun findActiveByUserId(userId: UserId): WorkoutPlan?
    fun findRecommendedPlans(goal: FitnessGoal?, experience: ExperienceLevel?): List<WorkoutPlan>
    fun findAllTemplates(): List<WorkoutPlan>
    fun existsById(id: PlanId): Boolean
}
