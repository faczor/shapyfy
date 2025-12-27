package com.shapyfy.core.boundary.plans

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.plan.*
import org.springframework.stereotype.Component

@Component
class PlanRestAdapter(
    private val planCreator: PlanCreator,
    private val planFetcher: PlanFetcher
) {
    fun createPlan(request: CreatePlanRequest, userId: UserId): PlanDetailsResponse {
        val command = request.toCommand(userId)
        val plan = planCreator.createPlan(command)
        return plan.toDetailsResponse()
    }

    fun getUserPlans(userId: UserId): UserPlansResponse {
        val plans = planFetcher.getUserPlans(userId)
        return UserPlansResponse(plans.map { it.toDetailsResponse() })
    }

    fun getPlanById(planId: String): PlanDetailsResponse {
        val plan = planFetcher.getPlanById(com.shapyfy.core.domain.PlanId.from(planId))
        return plan.toDetailsResponse()
    }

    fun getActivePlan(userId: UserId): PlanDetailsResponse {
        val plan = planFetcher.getActivePlan(userId)
        return plan.toDetailsResponse()
    }

    private fun CreatePlanRequest.toCommand(userId: UserId): PlanCreationCommand {
        val dayCreationData = days.map { dayRequest ->
            DayCreationData(
                dayIndex = dayRequest.dayIndex,
                name = dayRequest.name,
                type = DayType.valueOf(dayRequest.type),
                exercises = dayRequest.exercises.map { exerciseRequest ->
                    ExerciseCreationData(
                        exerciseId = ExerciseId.from(exerciseRequest.exerciseId),
                        orderIndex = exerciseRequest.orderIndex,
                        targetSets = exerciseRequest.targetSets,
                        targetReps = exerciseRequest.targetReps,
                        targetWeight = exerciseRequest.targetWeight,
                        notes = exerciseRequest.notes
                    )
                },
                notes = dayRequest.notes
            )
        }

        return PlanCreationCommand(
            userId = userId,
            name = name,
            description = description,
            days = dayCreationData
        )
    }
}
