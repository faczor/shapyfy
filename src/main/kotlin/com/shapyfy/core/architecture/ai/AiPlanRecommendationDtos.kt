package com.shapyfy.core.architecture.ai

import com.fasterxml.jackson.annotation.JsonProperty

data class AiPlanRecommendationResponse(
    @JsonProperty("plans")
    val plans: List<AiPlanRecommendationDto>
)

data class AiPlanRecommendationDto(
    @JsonProperty("name")
    val name: String,

    @JsonProperty("description")
    val description: String?,

    @JsonProperty("recommendation_score")
    val recommendationScore: Double,

    @JsonProperty("days")
    val days: List<AiDayDto>
)

data class AiDayDto(
    @JsonProperty("name")
    val name: String?,

    @JsonProperty("type")
    val type: String,

    @JsonProperty("exercises")
    val exercises: List<AiExerciseConfigDto>?
)

data class AiExerciseConfigDto(
    @JsonProperty("exercise_name")
    val exerciseName: String,

    @JsonProperty("sets")
    val sets: List<AiSetDto>,

    @JsonProperty("notes")
    val notes: String?
)

data class AiSetDto(
    @JsonProperty("reps")
    val reps: Int?,

    @JsonProperty("weight")
    val weight: Double?
)
