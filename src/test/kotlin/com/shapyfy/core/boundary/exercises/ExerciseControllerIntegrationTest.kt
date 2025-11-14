package com.shapyfy.core.boundary.exercises

import com.shapyfy.core.support.IntegrationTestBase
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ExerciseControllerIntegrationTest : IntegrationTestBase() {

    @Test
    fun `should create exercise`() {
        createExercise("Przysiady")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").isNotEmpty)
            .andExpect(jsonPath("$.name").value("Przysiady"))
            .andExpect(jsonPath("$.translationKey").value("exercises.squat"))
    }

    @Test
    fun `should return conflict when exercise exists`() {
        createExercise("Przysiady").andExpect(status().isOk)

        createExercise("Przysiady")
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value("Exercise with the same name already exists"))
            .andExpect(jsonPath("$.translationKey").value("exercises.squat"))
            .andExpect(jsonPath("$.availableTranslations.pl").value("Przysiady"))
            .andExpect(jsonPath("$.availableTranslations.en").value("Squat"))
    }

    @Test
    fun `should fail validation when name is blank`() {
        postJson("/api/v1/exercises", CreateExerciseRequest(name = ""))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should fallback to default language when header missing`() {
        postJson("/api/v1/exercises", CreateExerciseRequest(name = "Squat"), acceptLanguage = null)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Squat"))
            .andExpect(jsonPath("$.translationKey").value("exercises.squat"))
    }

    private fun createExercise(name: String): ResultActions =
        postJson("/api/v1/exercises", CreateExerciseRequest(name = name))
}
