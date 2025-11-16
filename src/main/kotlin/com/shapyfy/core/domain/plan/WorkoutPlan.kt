package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.PlanId
import com.shapyfy.core.domain.UserId
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class WorkoutPlan(
    val id: PlanId,
    val userId: UserId?,
    val name: String,
    val description: String?,
    val cycleDays: Int,
    val days: List<PlanDay>,
    val isActive: Boolean,
    val activationDate: LocalDate?,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    init {
        require(name.isNotBlank()) { "Plan name cannot be blank" }
        require(cycleDays in 1..30) { "Cycle days must be between 1 and 30" }
        require(days.size == cycleDays) { "Plan must have exactly $cycleDays days" }
        require(days.map { it.dayIndex }.toSet() == (0 until cycleDays).toSet()) {
            "Days must have unique indices from 0 to ${cycleDays - 1}"
        }
        if (isActive) {
            require(userId != null) { "Only user-specific plans can be active" }
            require(activationDate != null) { "Active plan must have activation date" }
        }
    }

    fun isTemplate(): Boolean = userId == null

    fun isUserPlan(): Boolean = userId != null

    fun getCurrentDayIndex(today: LocalDate = LocalDate.now()): Int {
        require(isActive && activationDate != null) {
            "Cannot calculate current day for inactive plan"
        }

        val daysSinceActivation = ChronoUnit.DAYS.between(activationDate, today)
        require(daysSinceActivation >= 0) {
            "Activation date cannot be in the future"
        }

        return (daysSinceActivation % cycleDays).toInt()
    }

    fun getTodaysPlanDay(today: LocalDate = LocalDate.now()): PlanDay {
        val dayIndex = getCurrentDayIndex(today)
        return days.first { it.dayIndex == dayIndex }
    }

    fun getWorkoutDays(): List<PlanDay> = days.filter { it.isWorkoutDay() }

    fun getRestDays(): List<PlanDay> = days.filter { it.isRestDay() }

    fun totalWorkoutDays(): Int = getWorkoutDays().size

    fun totalRestDays(): Int = getRestDays().size

    fun cloneForUser(newUserId: UserId): WorkoutPlan {
        require(isTemplate()) { "Can only clone templates" }
        return copy(
            id = PlanId.generate(),
            userId = newUserId,
            isActive = false,
            activationDate = null,
            createdAt = Instant.now(),
            updatedAt = null
        )
    }

    fun activate(activationDate: LocalDate = LocalDate.now()): WorkoutPlan {
        require(isUserPlan()) { "Cannot activate template" }
        return copy(
            isActive = true,
            activationDate = activationDate,
            updatedAt = Instant.now()
        )
    }

    fun deactivate(): WorkoutPlan {
        require(isActive) { "Plan is already inactive" }
        return copy(
            isActive = false,
            updatedAt = Instant.now()
        )
    }

    companion object {
        fun createUserPlan(
            userId: UserId,
            name: String,
            description: String?,
            days: List<PlanDay>
        ): WorkoutPlan {
            val cycleDays = days.size
            return WorkoutPlan(
                id = PlanId.generate(),
                userId = userId,
                name = name,
                description = description,
                cycleDays = cycleDays,
                days = days,
                isActive = false,
                activationDate = null,
                createdAt = Instant.now(),
                updatedAt = null
            )
        }

        fun createTemplate(
            name: String,
            description: String?,
            days: List<PlanDay>
        ): WorkoutPlan {
            val cycleDays = days.size
            return WorkoutPlan(
                id = PlanId.generate(),
                userId = null,
                name = name,
                description = description,
                cycleDays = cycleDays,
                days = days,
                isActive = false,
                activationDate = null,
                createdAt = Instant.now(),
                updatedAt = null
            )
        }
    }
}
