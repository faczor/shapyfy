package com.shapyfy.core.boundary.plans

import com.shapyfy.core.boundary.exercises.CreateExerciseRequest
import com.shapyfy.core.support.IntegrationTestBase
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class PlanControllerIntegrationTest : IntegrationTestBase() {

    private val testUserId = UUID.fromString("12345678-1234-1234-1234-123456789012")
    private val anotherUserId = UUID.fromString("87654321-4321-4321-4321-210987654321")

    @Test
    fun `should create workout plan successfully`() {
        // Given: Create exercises first
        val squatId = createExercise("Squat")
        val benchPressId = createExercise("Bench Press")
        val deadliftId = createExercise("Deadlift")

        // When: Create a workout plan
        val request = CreatePlanRequest(
            name = "Push Pull Legs",
            description = "6-day training split",
            days = listOf(
                // Day 1: Push
                CreatePlanDayRequest(
                    dayIndex = 0,
                    name = "Push Day",
                    type = "WORKOUT",
                    exercises = listOf(
                        CreatePlanExerciseRequest(
                            exerciseId = benchPressId.toString(),
                            orderIndex = 0,
                            targetSets = 4,
                            targetReps = 8,
                            targetWeight = 100.0,
                            notes = "Barbell bench press"
                        )
                    ),
                    notes = "Focus on chest and triceps"
                ),
                // Day 2: Pull
                CreatePlanDayRequest(
                    dayIndex = 1,
                    name = "Pull Day",
                    type = "WORKOUT",
                    exercises = listOf(
                        CreatePlanExerciseRequest(
                            exerciseId = deadliftId.toString(),
                            orderIndex = 0,
                            targetSets = 5,
                            targetReps = 5,
                            targetWeight = 140.0,
                            notes = null
                        )
                    ),
                    notes = null
                ),
                // Day 3: Legs
                CreatePlanDayRequest(
                    dayIndex = 2,
                    name = "Leg Day",
                    type = "WORKOUT",
                    exercises = listOf(
                        CreatePlanExerciseRequest(
                            exerciseId = squatId.toString(),
                            orderIndex = 0,
                            targetSets = 4,
                            targetReps = 10,
                            targetWeight = 120.0,
                            notes = "Deep squats"
                        )
                    ),
                    notes = "Focus on quads and glutes"
                ),
                // Day 4: Rest
                CreatePlanDayRequest(
                    dayIndex = 3,
                    name = "Active Recovery",
                    type = "REST",
                    exercises = emptyList(),
                    notes = "Light stretching or walking"
                )
            )
        )

        // Then: Plan is created successfully
        createPlan(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNotEmpty)
            .andExpect(jsonPath("$.name").value("Push Pull Legs"))
            .andExpect(jsonPath("$.description").value("6-day training split"))
            .andExpect(jsonPath("$.cycleDays").value(4))
            .andExpect(jsonPath("$.isActive").value(false))
            .andExpect(jsonPath("$.activationDate").isEmpty)
            .andExpect(jsonPath("$.days.length()").value(4))
            // Verify Day 1 (Push)
            .andExpect(jsonPath("$.days[0].dayIndex").value(0))
            .andExpect(jsonPath("$.days[0].name").value("Push Day"))
            .andExpect(jsonPath("$.days[0].type").value("WORKOUT"))
            .andExpect(jsonPath("$.days[0].exercises.length()").value(1))
            .andExpect(jsonPath("$.days[0].exercises[0].exerciseId").value(benchPressId.toString()))
            .andExpect(jsonPath("$.days[0].exercises[0].targetSets").value(4))
            .andExpect(jsonPath("$.days[0].exercises[0].targetReps").value(8))
            .andExpect(jsonPath("$.days[0].exercises[0].targetWeight").value(100.0))
            .andExpect(jsonPath("$.days[0].exercises[0].targetFormatted").value("4x8 @ 100.0kg"))
            .andExpect(jsonPath("$.days[0].exercises[0].notes").value("Barbell bench press"))
            .andExpect(jsonPath("$.days[0].notes").value("Focus on chest and triceps"))
            // Verify Day 4 (Rest)
            .andExpect(jsonPath("$.days[3].dayIndex").value(3))
            .andExpect(jsonPath("$.days[3].name").value("Active Recovery"))
            .andExpect(jsonPath("$.days[3].type").value("REST"))
            .andExpect(jsonPath("$.days[3].exercises.length()").value(0))
            .andExpect(jsonPath("$.days[3].notes").value("Light stretching or walking"))
    }

    @Test
    fun `should fail when creating plan with invalid exercise reference`() {
        // Given: A non-existent exercise ID
        val nonExistentExerciseId = UUID.randomUUID()

        // When: Try to create a plan with that exercise
        val request = CreatePlanRequest(
            name = "Invalid Plan",
            description = null,
            days = listOf(
                CreatePlanDayRequest(
                    dayIndex = 0,
                    name = "Day 1",
                    type = "WORKOUT",
                    exercises = listOf(
                        CreatePlanExerciseRequest(
                            exerciseId = nonExistentExerciseId.toString(),
                            orderIndex = 0,
                            targetSets = 3,
                            targetReps = 10,
                            targetWeight = null,
                            notes = null
                        )
                    ),
                    notes = null
                )
            )
        )

        // Then: Should return 400 Bad Request
        createPlan(request, testUserId)
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Invalid exercise reference in plan"))
            .andExpect(jsonPath("$.details").value("The exercise ID '$nonExistentExerciseId' does not exist"))
    }

    @Test
    fun `should retrieve plan by id`() {
        // Given: A created plan
        val squatId = createExercise("Squat")
        val createRequest = CreatePlanRequest(
            name = "Simple Plan",
            description = "Just squats",
            days = listOf(
                CreatePlanDayRequest(
                    dayIndex = 0,
                    name = "Squat Day",
                    type = "WORKOUT",
                    exercises = listOf(
                        CreatePlanExerciseRequest(
                            exerciseId = squatId.toString(),
                            orderIndex = 0,
                            targetSets = 5,
                            targetReps = 5,
                            targetWeight = 100.0,
                            notes = null
                        )
                    ),
                    notes = null
                )
            )
        )

        val planId = createPlan(createRequest, testUserId)
            .andReturn().response.contentAsString
            .let { objectMapper.readTree(it).get("id").asText() }

        // When: Retrieving the plan by ID
        getPlanById(planId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(planId))
            .andExpect(jsonPath("$.name").value("Simple Plan"))
            .andExpect(jsonPath("$.description").value("Just squats"))
            .andExpect(jsonPath("$.days.length()").value(1))
    }

    @Test
    fun `should return 404 when plan does not exist`() {
        val nonExistentId = UUID.randomUUID()

        getPlanById(nonExistentId.toString())
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Plan not found"))
    }

    @Test
    fun `should list all user plans`() {
        // Given: User has 3 plans
        val squatId = createExercise("Squat")

        repeat(3) { index ->
            val request = CreatePlanRequest(
                name = "Plan ${index + 1}",
                description = "Plan description ${index + 1}",
                days = listOf(
                    CreatePlanDayRequest(
                        dayIndex = 0,
                        name = "Day 1",
                        type = "WORKOUT",
                        exercises = listOf(
                            CreatePlanExerciseRequest(
                                exerciseId = squatId.toString(),
                                orderIndex = 0,
                                targetSets = 3,
                                targetReps = 10,
                                targetWeight = null,
                                notes = null
                            )
                        ),
                        notes = null
                    )
                )
            )
            createPlan(request, testUserId)
        }

        // When: Listing user plans
        getUserPlans(testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.plans.length()").value(3))
            .andExpect(jsonPath("$.plans[0].name").value("Plan 1"))
            .andExpect(jsonPath("$.plans[0].days.length()").value(1))
            .andExpect(jsonPath("$.plans[1].name").value("Plan 2"))
            .andExpect(jsonPath("$.plans[2].name").value("Plan 3"))
    }

    @Test
    fun `should return empty list when user has no plans`() {
        getUserPlans(testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.plans.length()").value(0))
    }

    @Test
    fun `should only show plans for authenticated user`() {
        // Given: Two users with plans
        val squatId = createExercise("Squat")
        val request = CreatePlanRequest(
            name = "User Plan",
            description = null,
            days = listOf(
                CreatePlanDayRequest(
                    dayIndex = 0,
                    name = "Day 1",
                    type = "WORKOUT",
                    exercises = listOf(
                        CreatePlanExerciseRequest(
                            exerciseId = squatId.toString(),
                            orderIndex = 0,
                            targetSets = 3,
                            targetReps = 10,
                            targetWeight = null,
                            notes = null
                        )
                    ),
                    notes = null
                )
            )
        )

        createPlan(request, testUserId)
        createPlan(request, anotherUserId)

        // When: First user lists their plans
        getUserPlans(testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.plans.length()").value(1))

        // When: Second user lists their plans
        getUserPlans(anotherUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.plans.length()").value(1))
    }

    @Test
    fun `should return 404 when user has no active plan`() {
        // Given: User has no active plan
        // When: Getting active plan
        getActivePlan(testUserId)
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("No active plan found"))
            .andExpect(jsonPath("$.details").value("User does not have an active plan"))
    }

    @Test
    fun `should create plan with multiple exercises per day`() {
        // Given: Multiple exercises
        val squatId = createExercise("Squat")
        val legPressId = createExercise("Leg Press")
        val legCurlId = createExercise("Leg Curl")

        // When: Create plan with multiple exercises on same day
        val request = CreatePlanRequest(
            name = "Leg Day",
            description = null,
            days = listOf(
                CreatePlanDayRequest(
                    dayIndex = 0,
                    name = "Legs",
                    type = "WORKOUT",
                    exercises = listOf(
                        CreatePlanExerciseRequest(
                            exerciseId = squatId.toString(),
                            orderIndex = 0,
                            targetSets = 4,
                            targetReps = 8,
                            targetWeight = 100.0,
                            notes = null
                        ),
                        CreatePlanExerciseRequest(
                            exerciseId = legPressId.toString(),
                            orderIndex = 1,
                            targetSets = 3,
                            targetReps = 12,
                            targetWeight = 150.0,
                            notes = null
                        ),
                        CreatePlanExerciseRequest(
                            exerciseId = legCurlId.toString(),
                            orderIndex = 2,
                            targetSets = 3,
                            targetReps = 15,
                            targetWeight = 50.0,
                            notes = null
                        )
                    ),
                    notes = null
                )
            )
        )

        // Then: Plan is created with all exercises in order
        createPlan(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.days[0].exercises.length()").value(3))
            .andExpect(jsonPath("$.days[0].exercises[0].exerciseId").value(squatId.toString()))
            .andExpect(jsonPath("$.days[0].exercises[0].orderIndex").value(0))
            .andExpect(jsonPath("$.days[0].exercises[1].exerciseId").value(legPressId.toString()))
            .andExpect(jsonPath("$.days[0].exercises[1].orderIndex").value(1))
            .andExpect(jsonPath("$.days[0].exercises[2].exerciseId").value(legCurlId.toString()))
            .andExpect(jsonPath("$.days[0].exercises[2].orderIndex").value(2))
    }

    // Helper methods

    private fun createExercise(name: String): UUID {
        val response = postJson("/api/v1/exercises", CreateExerciseRequest(name = name))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        return objectMapper.readTree(response).get("id").asText().let { UUID.fromString(it) }
    }

    private fun createPlan(request: CreatePlanRequest, userId: UUID): ResultActions =
        postJsonAuthenticated("/api/v1/plans", request, userId)

    private fun getPlanById(planId: String): ResultActions =
        getJsonAuthenticated("/api/v1/plans/$planId", testUserId)

    private fun getUserPlans(userId: UUID): ResultActions =
        getJsonAuthenticated("/api/v1/plans", userId)

    private fun getActivePlan(userId: UUID): ResultActions =
        getJsonAuthenticated("/api/v1/plans/active", userId)
}
