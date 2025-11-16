package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.PlanId
import com.shapyfy.core.domain.UserId

class PlanAlreadyActiveException(
    val userId: UserId,
    val existingActivePlan: WorkoutPlan
) : RuntimeException("User $userId already has an active plan: ${existingActivePlan.name}")

class PlanNotFoundException(
    val planId: PlanId
) : RuntimeException("Plan not found: $planId")

class NoActivePlanException(
    val userId: UserId
) : RuntimeException("No active plan found for user: $userId")

class OperationNotAllowedOnTemplateException(
    val planId: PlanId,
    val operation: String
) : RuntimeException("Cannot $operation on template plan: $planId")
