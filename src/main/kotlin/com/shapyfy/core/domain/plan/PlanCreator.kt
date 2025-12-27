package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.exercise.ExerciseRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PlanCreator(
    private val planRepository: PlanRepository,
    private val exerciseRepository: ExerciseRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun createPlan(command: PlanCreationCommand): WorkoutPlan {
        log.info("Creating plan for user: ${command.userId}, name: '${command.name}', days: ${command.days.size}")

        val allExerciseIds = command.days
            .flatMap { it.exercises }
            .map { it.exerciseId }
            .distinct()

        log.debug("Validating ${allExerciseIds.size} unique exercise references")
        val invalidExerciseIds = allExerciseIds.filter { !exerciseRepository.existsById(it) }
        if (invalidExerciseIds.isNotEmpty()) {
            log.error("Invalid exercise references found: $invalidExerciseIds")
            throw InvalidExerciseReferenceException(invalidExerciseIds.first())
        }

        val planDays = command.days.map { dayData ->
            val exercises = dayData.exercises.map { exerciseData ->
                val sets = exerciseData.sets.map { setData ->
                    ExerciseSet(
                        reps = setData.reps,
                        weight = setData.weight
                    )
                }
                PlanExercise.new(
                    exerciseId = exerciseData.exerciseId,
                    orderIndex = exerciseData.orderIndex,
                    sets = sets,
                    notes = exerciseData.notes
                )
            }

            when (dayData.type) {
                DayType.WORKOUT -> PlanDay.workout(
                    dayIndex = dayData.dayIndex,
                    name = dayData.name,
                    exercises = exercises,
                    notes = dayData.notes
                )
                DayType.REST -> PlanDay.rest(
                    dayIndex = dayData.dayIndex,
                    name = dayData.name,
                    notes = dayData.notes
                )
            }
        }

        val plan = WorkoutPlan.createUserPlan(
            userId = command.userId,
            name = command.name,
            description = command.description,
            days = planDays
        )

        log.debug("Saving plan with ${planDays.size} days (${plan.totalWorkoutDays()} workout, ${plan.totalRestDays()} rest)")
        val savedPlan = planRepository.save(plan)

        log.info("Plan created successfully: id=${savedPlan.id}, cycleDays=${savedPlan.cycleDays}")
        return savedPlan
    }
}

class InvalidExerciseReferenceException(
    val exerciseId: com.shapyfy.core.domain.ExerciseId
) : RuntimeException("Exercise not found: $exerciseId")
