package com.shapyfy.core.domain

import com.shapyfy.core.domain.model.Exercise
import com.shapyfy.core.domain.model.Language

interface ExerciseRepository {
    fun save(exercise: Exercise): Exercise
    fun findByName(name: String): Exercise?
    fun findAll(): List<Exercise>
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
