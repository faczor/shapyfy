package com.shapyfy.core.domain.exercise

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language

interface ExerciseRepository {
    fun saveNew(exercise: Exercise, language: Language, detectedLanguage: Language, confidence: Double): Exercise

    fun findByName(name: String): Exercise?
    fun findAll(): List<Exercise>
    fun existsById(id: ExerciseId): Boolean
}

interface ExerciseTranslationPort {
    fun canonicalize(rawName: String, preferredLanguage: Language): CanonicalizationResult

    fun storeTranslations(
        exercise: Exercise,
        rawName: String,
        preferredLanguage: Language,
        detectedLanguage: Language
    ): String

    fun localize(exercise: Exercise, language: Language): String
}

data class CanonicalizationResult(
    val canonicalName: String,
    val detectedLanguage: Language,
    val confidence: Double
)

interface ExerciseClassificationPort {
    fun classify(exerciseName: String): ExerciseProperties
}

/**
 * Domain representation of exercise properties determined by classification.
 */
data class ExerciseProperties(
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: Set<MuscleGroup>,
    val equipmentRequired: Set<Equipment>,
    val difficulty: Difficulty,
    val movementPattern: MovementPattern
)
