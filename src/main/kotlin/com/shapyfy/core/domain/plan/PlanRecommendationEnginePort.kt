package com.shapyfy.core.domain.plan

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.exercise.Exercise

interface PlanRecommendationEnginePort {
    fun generateRecommendation(
        availableExercises: List<Exercise>,
        goal: FitnessGoal,
        experience: ExperienceLevel,
        location: WorkoutLocation,
        frequency: Int
    ): PlanRecommendation
}

data class PlanRecommendation(
    val name: String,
    val description: String?,
    val recommendationScore: Double,
    val days: List<RecommendedDay>
)

data class RecommendedDay(
    val name: String?,
    val type: DayType,
    val exercises: List<RecommendedExercise>
)

data class RecommendedExercise(
    val exerciseId: ExerciseId,
    val sets: List<RecommendedSet>,
    val notes: String?
)

data class RecommendedSet(
    val reps: Int?,
    val weight: Double?
)
