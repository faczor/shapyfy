package com.shapyfy.core.boundary.exercises

import java.util.UUID

data class ExerciseResponse(
    val id: UUID,
    val name: String,
    val translationKey: String
)

data class ExerciseConflictResponse(
    val id: UUID,
    val message: String,
    val translationKey: String,
    val availableTranslations: Map<String, String>
)
