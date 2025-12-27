package com.shapyfy.core.architecture.persistence

import com.shapyfy.core.architecture.metrics.PlanMetrics
import com.shapyfy.core.domain.*
import com.shapyfy.core.domain.plan.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.UUID

@Repository
class PlanJdbcRepository(
    private val planCrudRepository: PlanCrudRepository,
    private val planDayCrudRepository: PlanDayCrudRepository,
    private val planExerciseCrudRepository: PlanExerciseCrudRepository,
    private val exerciseSetCrudRepository: ExerciseSetCrudRepository,
    private val planMetrics: PlanMetrics,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : PlanRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun save(plan: WorkoutPlan): WorkoutPlan {
        log.info("Attempting to persist plan '{}' for user '{}'", plan.id, plan.userId)

        val planEntity = PlanEntity.new(
            id = plan.id.value,
            userId = plan.userId?.value,
            name = plan.name,
            description = plan.description,
            cycleDays = plan.cycleDays,
            isActive = plan.isActive,
            activationDate = plan.activationDate,
            isRecommended = plan.isRecommended,
            recommendationScore = plan.recommendationScore,
            targetExperienceLevel = plan.targetExperienceLevel?.name,
            targetGoal = plan.targetGoal?.name,
            createdAt = plan.createdAt,
            updatedAt = plan.updatedAt
        )
        planCrudRepository.save(planEntity)

        plan.days.forEach { day ->
            val dayEntity = PlanDayEntity.new(
                id = day.id.value,
                planId = plan.id.value,
                dayIndex = day.dayIndex,
                name = day.name,
                dayType = day.type.name,
                notes = day.notes
            )
            planDayCrudRepository.save(dayEntity)

            day.exercises.forEach { exercise ->
                val exerciseEntity = PlanExerciseEntity.new(
                    id = exercise.id.value,
                    planDayId = day.id.value,
                    exerciseId = exercise.exerciseId.value,
                    orderIndex = exercise.orderIndex,
                    notes = exercise.notes
                )
                planExerciseCrudRepository.save(exerciseEntity)

                exercise.sets.forEachIndexed { index, set ->
                    val setEntity = ExerciseSetEntity.new(
                        id = UUID.randomUUID(),
                        planExerciseId = exercise.id.value,
                        setIndex = index,
                        reps = set.reps,
                        weight = set.weight
                    )
                    exerciseSetCrudRepository.save(setEntity)
                }
            }
        }

        log.info("Plan '{}' persisted successfully with {} days", plan.id, plan.days.size)

        if (plan.userId != null) {
            planMetrics.recordPlanCreated(
                userId = plan.userId,
                cycleDays = plan.cycleDays,
                workoutDays = plan.totalWorkoutDays()
            )
        }

        return plan
    }

    override fun findById(id: PlanId): WorkoutPlan? {
        log.info("Attempting to load plan with id '{}'", id)
        val planEntity = planCrudRepository.findById(id.value).orElse(null) ?: run {
            log.info("Plan '{}' not found", id)
            return null
        }

        val plan = loadPlanWithDays(planEntity)
        log.info("Plan '{}' loaded successfully", id)
        return plan
    }

    override fun findAllByUserId(userId: UserId): List<WorkoutPlan> {
        log.info("Attempting to load all plans for user '{}'", userId)
        val planEntities = planCrudRepository.findAllByUserId(userId.value)
        val plans = loadPlansWithDays(planEntities)
        log.info("Loaded {} plans for user '{}'", plans.size, userId)
        return plans
    }

    override fun findActiveByUserId(userId: UserId): WorkoutPlan? {
        log.info("Attempting to load active plan for user '{}'", userId)
        val planEntity = planCrudRepository.findByUserIdAndIsActive(userId.value, true) ?: run {
            log.info("No active plan found for user '{}'", userId)
            return null
        }

        val plan = loadPlanWithDays(planEntity)
        log.info("Active plan '{}' loaded for user '{}'", plan.id, userId)
        return plan
    }

    override fun findAllTemplates(): List<WorkoutPlan> {
        log.info("Attempting to load all global templates")

        val templateEntities = planCrudRepository.findAllTemplates()
        val templates = loadPlansWithDays(templateEntities)
        log.info("Loaded {} global templates", templates.size)
        return templates
    }

    override fun existsById(id: PlanId): Boolean {
        log.info("Checking if plan exists with id '{}'", id)
        val exists = planCrudRepository.existsById(id.value)
        log.info("Plan '{}' exists: {}", id, exists)
        return exists
    }

    private fun loadPlanWithDays(planEntity: PlanEntity): WorkoutPlan {
        val dayEntities = planDayCrudRepository.findByPlanId(planEntity.getId())
        val dayIds = dayEntities.map { it.getId() }
        val exerciseEntities = planExerciseCrudRepository.findByPlanDayIdIn(dayIds)
        val exerciseIds = exerciseEntities.map { it.getId() }
        val setEntities = exerciseSetCrudRepository.findByPlanExerciseIdIn(exerciseIds)

        val setsByExerciseId = setEntities.groupBy { it.planExerciseId }
        val exercisesByDayId = exerciseEntities.groupBy { it.planDayId }

        val days = dayEntities.map { dayEntity ->
            val exercises = exercisesByDayId[dayEntity.getId()]
                ?.map { it.toDomain(setsByExerciseId[it.getId()] ?: emptyList()) }
                ?.sortedBy { it.orderIndex }
                ?: emptyList()

            dayEntity.toDomain(exercises)
        }.sortedBy { it.dayIndex }

        return planEntity.toDomain(days)
    }

    private fun loadPlansWithDays(planEntities: List<PlanEntity>): List<WorkoutPlan> {
        if (planEntities.isEmpty()) return emptyList()

        val planIds = planEntities.map { it.getId() }
        val allDayEntities = planDayCrudRepository.findByPlanIdIn(planIds)
        val dayIds = allDayEntities.map { it.getId() }
        val allExerciseEntities = planExerciseCrudRepository.findByPlanDayIdIn(dayIds)
        val exerciseIds = allExerciseEntities.map { it.getId() }
        val allSetEntities = exerciseSetCrudRepository.findByPlanExerciseIdIn(exerciseIds)

        val setsByExerciseId = allSetEntities.groupBy { it.planExerciseId }
        val exercisesByDayId = allExerciseEntities.groupBy { it.planDayId }
        val daysByPlanId = allDayEntities.groupBy { it.planId }

        return planEntities.map { planEntity ->
            val planId = planEntity.getId()
            val dayEntities = daysByPlanId[planId] ?: emptyList()

            val days = dayEntities.map { dayEntity ->
                val exercises = exercisesByDayId[dayEntity.getId()]
                    ?.map { it.toDomain(setsByExerciseId[it.getId()] ?: emptyList()) }
                    ?.sortedBy { it.orderIndex }
                    ?: emptyList()

                dayEntity.toDomain(exercises)
            }.sortedBy { it.dayIndex }

            planEntity.toDomain(days)
        }
    }

    private fun PlanEntity.toDomain(days: List<PlanDay>): WorkoutPlan =
        WorkoutPlan(
            id = PlanId.from(getId()),
            userId = userId?.let { UserId.from(it) },
            name = name,
            description = description,
            cycleDays = cycleDays,
            days = days,
            isActive = isActive,
            activationDate = activationDate,
            isRecommended = isRecommended,
            recommendationScore = recommendationScore,
            targetExperienceLevel = targetExperienceLevel?.let { ExperienceLevel.valueOf(it) },
            targetGoal = targetGoal?.let { FitnessGoal.valueOf(it) },
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    private fun PlanDayEntity.toDomain(exercises: List<PlanExercise>): PlanDay =
        PlanDay(
            id = PlanDayId.from(getId()),
            dayIndex = dayIndex,
            name = name,
            type = DayType.valueOf(dayType),
            exercises = exercises,
            notes = notes
        )

    private fun PlanExerciseEntity.toDomain(setEntities: List<ExerciseSetEntity>): PlanExercise {
        val sets = setEntities
            .sortedBy { it.setIndex }
            .map { ExerciseSet(reps = it.reps, weight = it.weight) }

        return PlanExercise(
            id = PlanExerciseId.from(getId()),
            exerciseId = ExerciseId.from(exerciseId),
            orderIndex = orderIndex,
            sets = sets,
            notes = notes
        )
    }
}
