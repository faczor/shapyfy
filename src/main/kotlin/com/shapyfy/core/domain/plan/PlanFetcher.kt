package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.PlanId
import com.shapyfy.core.domain.UserId
import org.springframework.stereotype.Service

@Service
class PlanFetcher(
    private val planRepository: PlanRepository
) {
    fun getUserPlans(userId: UserId): List<WorkoutPlan> {
        return planRepository.findAllByUserId(userId)
    }

    fun getPlanById(planId: PlanId): WorkoutPlan {
        return planRepository.findById(planId)
            ?: throw PlanNotFoundException(planId)
    }

    fun getActivePlan(userId: UserId): WorkoutPlan {
        return planRepository.findActiveByUserId(userId)
            ?: throw NoActivePlanException(userId)
    }

    fun getAllTemplates(): List<WorkoutPlan> {
        return planRepository.findAllTemplates()
    }
}
