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
            .andExpect(jsonPath("$.translation_key").value("exercises.squat"))
            .andExpect(jsonPath("$.primary_muscle_group").value("QUADS"))
            .andExpect(jsonPath("$.secondary_muscle_groups").isArray)
            .andExpect(jsonPath("$.equipment_required").isArray)
            .andExpect(jsonPath("$.equipment_required[0]").value("BODYWEIGHT"))
            .andExpect(jsonPath("$.difficulty").value("BEGINNER"))
            .andExpect(jsonPath("$.movement_pattern").value("SQUAT"))
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
            .andExpect(jsonPath("$.translation_key").value("exercises.squat"))
            .andExpect(jsonPath("$.primary_muscle_group").value("QUADS"))
            .andExpect(jsonPath("$.difficulty").value("BEGINNER"))
    }

    @Test
    fun `should list all exercises`() {
        createExercise("Przysiady").andExpect(status().isOk)
        createExercise("Wyciskanie").andExpect(status().isOk)

        getJson("/api/v1/exercises")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exercises").isArray)
            .andExpect(jsonPath("$.exercises.length()").value(2))
            .andExpect(jsonPath("$.exercises[0].id").isNotEmpty)
            .andExpect(jsonPath("$.exercises[0].name").isNotEmpty)
            .andExpect(jsonPath("$.exercises[0].translation_key").isNotEmpty)
            .andExpect(jsonPath("$.exercises[0].primary_muscle_group").isNotEmpty)
            .andExpect(jsonPath("$.exercises[0].secondary_muscle_groups").isArray)
            .andExpect(jsonPath("$.exercises[0].equipment_required").isArray)
            .andExpect(jsonPath("$.exercises[0].difficulty").isNotEmpty)
            .andExpect(jsonPath("$.exercises[0].movement_pattern").isNotEmpty)
            .andExpect(jsonPath("$.exercises[1].id").isNotEmpty)
            .andExpect(jsonPath("$.exercises[1].name").isNotEmpty)
            .andExpect(jsonPath("$.exercises[1].translation_key").isNotEmpty)
    }

    @Test
    fun `should return empty list when no exercises exist`() {
        getJson("/api/v1/exercises")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exercises").isArray)
            .andExpect(jsonPath("$.exercises.length()").value(0))
    }

    @Test
    fun `should return exercises with localized names based on Accept-Language`() {
        // Create exercise with Polish name - this will store both PL and EN translations
        createExercise("Przysiady").andExpect(status().isOk)

        // When requesting with PL, should get Polish name
        getJson("/api/v1/exercises", acceptLanguage = "pl")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exercises[0].name").value("Przysiady"))

        // When requesting with EN, should get English name
        getJson("/api/v1/exercises", acceptLanguage = "en")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.exercises[0].name").value("Squat"))
    }

    private fun createExercise(name: String): ResultActions =
        postJson("/api/v1/exercises", CreateExerciseRequest(name = name))
}
