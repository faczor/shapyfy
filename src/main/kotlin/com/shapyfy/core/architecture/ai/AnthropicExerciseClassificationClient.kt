package com.shapyfy.core.architecture.ai

import com.fasterxml.jackson.annotation.JsonProperty
import com.shapyfy.core.architecture.classification.ExerciseClassification
import com.shapyfy.core.architecture.classification.ExerciseClassificationClient
import com.shapyfy.core.domain.exercise.Difficulty
import com.shapyfy.core.domain.exercise.Equipment
import com.shapyfy.core.domain.exercise.MovementPattern
import com.shapyfy.core.domain.exercise.MuscleGroup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Anthropic Claude-powered exercise classifier.
 * Uses AI to analyze exercise names and determine their properties.
 */
@Component
class AnthropicExerciseClassificationClient(
    private val anthropicClient: AnthropicClient
) : ExerciseClassificationClient {

    private val logger = LoggerFactory.getLogger(AnthropicExerciseClassificationClient::class.java)

    override fun classify(exerciseName: String): ExerciseClassification {
        logger.info("Classifying exercise: '{}'", exerciseName)

        return try {
            val prompt = buildClassificationPrompt(exerciseName)
            val response = anthropicClient.sendPrompt<ClassificationResponse>(prompt)

            ExerciseClassification(
                primaryMuscleGroup = parseMuscleGroup(response.primaryMuscleGroup),
                secondaryMuscleGroups = response.secondaryMuscleGroups.mapNotNull {
                    parseMuscleGroupOrNull(it)
                }.toSet(),
                equipmentRequired = response.equipmentRequired.mapNotNull {
                    parseEquipmentOrNull(it)
                }.toSet(),
                difficulty = parseDifficulty(response.difficulty),
                movementPattern = parseMovementPattern(response.movementPattern)
            )
        } catch (e: Exception) {
            logger.error("Failed to classify exercise '{}', using fallback defaults", exerciseName, e)
            fallbackClassification()
        }
    }

    private fun buildClassificationPrompt(exerciseName: String): String = """
        You are an expert strength and conditioning coach. Analyze the following exercise and classify it according to these exact categories.

        Exercise name: "$exerciseName"

        Classify the exercise by providing:

        1. primary_muscle_group: The MAIN muscle group targeted (choose ONE):
           - CHEST
           - BACK
           - SHOULDERS
           - BICEPS
           - TRICEPS
           - QUADS
           - HAMSTRINGS
           - GLUTES
           - CALVES
           - CORE
           - FOREARMS

        2. secondary_muscle_groups: Supporting muscles involved (array of muscle groups from above list, can be empty)

        3. equipment_required: All equipment needed (array, choose from):
           - BODYWEIGHT
           - DUMBBELLS
           - BARBELL
           - KETTLEBELL
           - RESISTANCE_BANDS
           - PULL_UP_BAR
           - BENCH
           - CABLE_MACHINE
           - SMITH_MACHINE

        4. difficulty: Skill/technique requirement (choose ONE):
           - BEGINNER (simple movements, easy to learn)
           - ADVANCED (complex movements, requires technique/coordination)

        5. movement_pattern: Primary movement type (choose ONE):
           - PUSH (pressing movements: chest, shoulders, triceps)
           - PULL (pulling movements: back, biceps)
           - SQUAT (quad-dominant leg movements)
           - HINGE (hip-dominant movements like deadlifts)
           - ISOLATION (single-joint exercises)

        Return ONLY a JSON object with these exact field names:
        {
          "primary_muscle_group": "CHEST",
          "secondary_muscle_groups": ["TRICEPS", "SHOULDERS"],
          "equipment_required": ["BARBELL", "BENCH"],
          "difficulty": "BEGINNER",
          "movement_pattern": "PUSH"
        }

        Be specific and accurate based on standard exercise biomechanics.
    """.trimIndent()

    private data class ClassificationResponse(
        @JsonProperty("primary_muscle_group")
        val primaryMuscleGroup: String,

        @JsonProperty("secondary_muscle_groups")
        val secondaryMuscleGroups: List<String>,

        @JsonProperty("equipment_required")
        val equipmentRequired: List<String>,

        val difficulty: String,

        @JsonProperty("movement_pattern")
        val movementPattern: String
    )

    private fun parseMuscleGroup(value: String): MuscleGroup {
        return parseMuscleGroupOrNull(value)
            ?: throw IllegalArgumentException("Invalid muscle group: $value")
    }

    private fun parseMuscleGroupOrNull(value: String): MuscleGroup? {
        return try {
            MuscleGroup.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid muscle group value: {}", value)
            null
        }
    }

    private fun parseEquipmentOrNull(value: String): Equipment? {
        return try {
            Equipment.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid equipment value: {}", value)
            null
        }
    }

    private fun parseDifficulty(value: String): Difficulty {
        return try {
            Difficulty.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid difficulty value: {}, defaulting to BEGINNER", value)
            Difficulty.BEGINNER
        }
    }

    private fun parseMovementPattern(value: String): MovementPattern {
        return try {
            MovementPattern.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid movement pattern value: {}, defaulting to ISOLATION", value)
            MovementPattern.ISOLATION
        }
    }

    private fun fallbackClassification(): ExerciseClassification {
        return ExerciseClassification(
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = emptySet(),
            equipmentRequired = setOf(Equipment.BODYWEIGHT),
            difficulty = Difficulty.BEGINNER,
            movementPattern = MovementPattern.ISOLATION
        )
    }
}
