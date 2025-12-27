package com.shapyfy.core.boundary.recommendations

import com.shapyfy.core.boundary.plans.PlanDetailsResponse
import com.shapyfy.core.boundary.plans.toDetailsResponse
import com.shapyfy.core.domain.exercise.Equipment
import com.shapyfy.core.domain.plan.PlanRecommendationCreator
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/recommendations/plans")
class PlanRecommendationController(
    private val planRecommendationCreator: PlanRecommendationCreator
) {

    @PostMapping
    fun getRecommendedPlans(
        @Valid @RequestBody request: CreatePlanRecommendationRequest
    ): ResponseEntity<List<PlanDetailsResponse>> {

        val plans = planRecommendationCreator.create(
            goal = request.goal,
            experience = request.experience,
            location = request.location,
            frequency = request.frequency,
            availableAccessories = request.availableAccessories?.takeIf { it.isNotEmpty() } ?: Equipment.all()
        )

        return ResponseEntity.ok(plans.map { it.toDetailsResponse() })
    }
}
