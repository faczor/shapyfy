package com.shapyfy.core.boundary.plans

import com.shapyfy.core.architecture.config.JwtToken
import com.shapyfy.core.boundary.ApiController
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/plans")
class PlanController(
    private val planRestAdapter: PlanRestAdapter
) : ApiController() {

    @PostMapping
    fun createPlan(
        @Valid @RequestBody request: CreatePlanRequest,
        jwt: JwtToken
    ): ResponseEntity<PlanDetailsResponse> {
        val response = planRestAdapter.createPlan(request, jwt.userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getUserPlans(
        jwt: JwtToken
    ): ResponseEntity<UserPlansResponse> {
        val response = planRestAdapter.getUserPlans(jwt.userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getPlanById(
        @PathVariable id: String
    ): ResponseEntity<PlanDetailsResponse> {
        val plan = planRestAdapter.getPlanById(id)
        return ResponseEntity.ok(plan)
    }

    @GetMapping("/active")
    fun getActivePlan(
        jwt: JwtToken
    ): ResponseEntity<PlanDetailsResponse> {
        val plan = planRestAdapter.getActivePlan(jwt.userId)
        return ResponseEntity.ok(plan)
    }
}
