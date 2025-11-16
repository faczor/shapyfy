package com.shapyfy.core.architecture.metrics

import com.shapyfy.core.domain.PlanId
import com.shapyfy.core.domain.UserId
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

/**
 * Architecture-layer metrics for plan operations.
 * This is NOT a domain port - it's a pure infrastructure concern.
 */
@Component
class PlanMetrics(
    private val meterRegistry: MeterRegistry
) {

    fun recordPlanCreated(userId: UserId, cycleDays: Int, workoutDays: Int) {
        meterRegistry.counter(
            "shapyfy.plan.created",
            "cycle_days", cycleDays.toString(),
            "workout_days", workoutDays.toString()
        ).increment()
    }

    fun recordPlanActivated(userId: UserId, planId: PlanId) {
        meterRegistry.counter("shapyfy.plan.activated").increment()
    }

    fun recordPlanDeactivated(userId: UserId, planId: PlanId) {
        meterRegistry.counter("shapyfy.plan.deactivated").increment()
    }

    fun recordTemplateCloned(userId: UserId, templateId: PlanId) {
        meterRegistry.counter("shapyfy.plan.template_cloned").increment()
    }
}
