package com.shapyfy.core.boundary.recommendations

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.shapyfy.core.boundary.plans.PlanDetailsResponse

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PlanRecommendationResponse(
    val available: Boolean,
    val plan: PlanDetailsResponse?,
    val reason: String?
) {
    companion object {
        fun success(plan: PlanDetailsResponse) = PlanRecommendationResponse(
            available = true,
            plan = plan,
            reason = null
        )

        fun unavailable(reason: String) = PlanRecommendationResponse(
            available = false,
            plan = null,
            reason = reason
        )
    }
}
