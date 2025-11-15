package com.shapyfy.core.domain.exercise

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language

interface ExerciseRepository {
    fun save(exercise: Exercise): Exercise
    fun findByName(name: String): Exercise?
    fun findAll(): List<Exercise>
    fun existsById(id: ExerciseId): Boolean
}

interface ExerciseMetricsPort {
    fun recordCreation(language: Language, detectedLanguage: Language, confidence: Double)
    fun recordConflict(language: Language)
}

interface ExerciseTranslationPort {
    fun canonicalize(rawName: String, preferredLanguage: Language): CanonicalizationResult
    fun storeTranslations(
        exercise: Exercise,
        rawName: String,
        preferredLanguage: Language,
        detectedLanguage: Language
    )
    fun localize(exercise: Exercise, language: Language): String
}

data class CanonicalizationResult(
    val canonicalName: String,
    val detectedLanguage: Language,
    val confidence: Double,
    val isExistingTranslation: Boolean
)
