package com.shapyfy.core.architecture.ai

import com.shapyfy.core.domain.exercise.Exercise
import com.shapyfy.core.domain.plan.*
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class AiPlanRecommendationEngine(
    private val anthropicClient: AnthropicClient
) : PlanRecommendationEnginePort {

    private val systemPrompt: String by lazy {
        loadPromptFromResource("prompts/plan-recommendation-system.xml")
    }

    private val userPromptTemplate: String by lazy {
        loadPromptFromResource("prompts/plan-recommendation-user.xml")
    }

    override fun generateRecommendation(
        availableExercises: List<Exercise>,
        goal: FitnessGoal,
        experience: ExperienceLevel,
        location: WorkoutLocation,
        frequency: Int
    ): PlanRecommendation {
        val prompt = buildPrompt(availableExercises, goal, experience, location, frequency)

        val response = anthropicClient.sendPromptWithSystem<AiPlanRecommendationResponse>(
            systemPrompt = systemPrompt,
            userPrompt = prompt,
            model = AnthropicClient.DEFAULT_MODEL,
            maxTokens = 4096,
            temperature = 0.7
        )

        return mapToPlanRecommendation(response.plans.first(), availableExercises)
    }

    private fun loadPromptFromResource(path: String): String {
        return ClassPathResource(path).inputStream.bufferedReader().use { it.readText() }
    }

    private fun buildPrompt(
        exercises: List<Exercise>,
        goal: FitnessGoal,
        experience: ExperienceLevel,
        location: WorkoutLocation,
        frequency: Int
    ): String {
        val exerciseList = exercises.joinToString("\n") { exercise ->
            "- ${exercise.canonicalName.value} (Primary: ${exercise.primaryMuscleGroup}, Equipment: ${exercise.equipmentRequired.joinToString()}, Difficulty: ${exercise.difficulty})"
        }

        return userPromptTemplate
            .replace("{{goal}}", goal.toString())
            .replace("{{experience}}", experience.toString())
            .replace("{{location}}", location.toString())
            .replace("{{frequency}}", frequency.toString())
            .replace("{{exerciseList}}", exerciseList)
    }

    private fun mapToPlanRecommendation(
        aiPlan: AiPlanRecommendationDto,
        availableExercises: List<Exercise>
    ): PlanRecommendation {
        val exerciseMap = availableExercises.associateBy { it.canonicalName.value }

        val days = aiPlan.days.map { aiDay ->
            val dayType = when (aiDay.type.uppercase()) {
                "WORKOUT" -> DayType.WORKOUT
                "REST" -> DayType.REST
                else -> throw IllegalArgumentException("Invalid day type: ${aiDay.type}")
            }

            val exercises = if (dayType == DayType.REST) {
                emptyList()
            } else {
                (aiDay.exercises ?: emptyList()).mapNotNull { aiExercise ->
                    val exercise = exerciseMap[aiExercise.exerciseName]
                    if (exercise == null) {
                        null
                    } else {
                        RecommendedExercise(
                            exerciseId = exercise.id,
                            sets = aiExercise.sets.map { aiSet ->
                                RecommendedSet(
                                    reps = aiSet.reps,
                                    weight = aiSet.weight
                                )
                            },
                            notes = aiExercise.notes
                        )
                    }
                }
            }

            RecommendedDay(
                name = aiDay.name,
                type = dayType,
                exercises = exercises
            )
        }

        return PlanRecommendation(
            name = aiPlan.name,
            description = aiPlan.description,
            recommendationScore = aiPlan.recommendationScore,
            days = days
        )
    }
}
