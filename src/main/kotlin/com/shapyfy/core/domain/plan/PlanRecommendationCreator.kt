package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.PlanDayId
import com.shapyfy.core.domain.PlanExerciseId
import com.shapyfy.core.domain.exercise.Equipment
import com.shapyfy.core.domain.exercise.ExerciseRepository
import org.springframework.stereotype.Service

@Service
class PlanRecommendationCreator(
    private val exerciseRepository: ExerciseRepository,
    private val planRecommendationEnginePort: PlanRecommendationEnginePort,
    private val planRepository: PlanRepository
) {
    fun create(
        goal: FitnessGoal,
        experience: ExperienceLevel,
        location: WorkoutLocation,
        frequency: Int,
        availableAccessories: List<Equipment>
    ): List<WorkoutPlan> {
        val applicableExercises = exerciseRepository.findByEquipment(availableAccessories)

        if (applicableExercises.isEmpty()) {
            return emptyList()
        }

        val recommendation = planRecommendationEnginePort.generateRecommendation(
            availableExercises = applicableExercises,
            goal = goal,
            experience = experience,
            location = location,
            frequency = frequency
        )

        val workoutPlan = mapToWorkoutPlan(recommendation, goal, experience)
        val savedPlan = planRepository.save(workoutPlan)

        return listOf(savedPlan)
    }

    private fun mapToWorkoutPlan(
        recommendation: PlanRecommendation,
        goal: FitnessGoal,
        experience: ExperienceLevel
    ): WorkoutPlan {
        val days = recommendation.days.mapIndexed { index, recommendedDay ->
            val exercises = recommendedDay.exercises.mapIndexed { exerciseIndex, recommendedExercise ->
                PlanExercise(
                    id = PlanExerciseId.generate(),
                    exerciseId = recommendedExercise.exerciseId,
                    orderIndex = exerciseIndex,
                    sets = recommendedExercise.sets.map { set ->
                        ExerciseSet(
                            reps = set.reps,
                            weight = set.weight
                        )
                    },
                    notes = recommendedExercise.notes
                )
            }

            PlanDay(
                id = PlanDayId.generate(),
                dayIndex = index,
                name = recommendedDay.name,
                type = recommendedDay.type,
                exercises = exercises,
                notes = null
            )
        }

        return WorkoutPlan.createTemplate(
            name = recommendation.name,
            description = recommendation.description,
            days = days,
            isRecommended = true,
            recommendationScore = recommendation.recommendationScore,
            targetExperienceLevel = experience,
            targetGoal = goal
        )
    }
}