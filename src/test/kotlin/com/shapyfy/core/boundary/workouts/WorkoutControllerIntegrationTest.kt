package com.shapyfy.core.boundary.workouts

import com.shapyfy.core.boundary.exercises.CreateExerciseRequest
import com.shapyfy.core.support.IntegrationTestBase
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.UUID

class WorkoutControllerIntegrationTest : IntegrationTestBase() {

    private val testUserId = UUID.fromString("12345678-1234-1234-1234-123456789012")
    private val anotherUserId = UUID.fromString("87654321-4321-4321-4321-210987654321")

    @Test
    fun `should log workout successfully`() {
        // Given: Create an exercise first
        val exerciseId = createExercise("Squat")

        // When: Log a workout with that exercise
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(
                            setNumber = 1,
                            weight = 100.0,
                            reps = 10,
                            timestamp = now.minusSeconds(3500).toEpochMilli()
                        ),
                        WorkoutSetRequest(
                            setNumber = 2,
                            weight = 100.0,
                            reps = 8,
                            timestamp = now.minusSeconds(3300).toEpochMilli()
                        )
                    )
                )
            )
        )

        // Then: Workout is created successfully
        logWorkout(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNotEmpty)
            .andExpect(jsonPath("$.user_id").value(testUserId.toString()))
            .andExpect(jsonPath("$.status").value("COMPLETED"))
            .andExpect(jsonPath("$.total_sets").value(2))
            .andExpect(jsonPath("$.total_volume").value(1800.0)) // (100*10) + (100*8)
            .andExpect(jsonPath("$.exercises[0].exercise_id").value(exerciseId.toString()))
            .andExpect(jsonPath("$.exercises[0].sets.length()").value(2))
            .andExpect(jsonPath("$.exercises[0].sets[0].set_number").value(1))
            .andExpect(jsonPath("$.exercises[0].sets[0].weight").value(100.0))
            .andExpect(jsonPath("$.exercises[0].sets[0].reps").value(10))
    }

    @Test
    fun `should log workout with multiple exercises`() {
        // Given: Create multiple exercises
        val squatId = createExercise("Squat")
        val benchPressId = createExercise("Bench Press")

        // When: Log a workout with both exercises
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = squatId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(setNumber = 1, weight = 100.0, reps = 10, timestamp = now.minusSeconds(3500).toEpochMilli())
                    )
                ),
                WorkoutExerciseRequest(
                    exerciseId = benchPressId,
                    orderIndex = 1,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(setNumber = 1, weight = 80.0, reps = 10, timestamp = now.minusSeconds(3000).toEpochMilli())
                    )
                )
            )
        )

        // Then: Workout is created with both exercises
        logWorkout(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.total_sets").value(2))
            .andExpect(jsonPath("$.total_volume").value(1800.0)) // (100*10) + (80*10)
            .andExpect(jsonPath("$.exercises.length()").value(2))
            .andExpect(jsonPath("$.exercises[0].exercise_id").value(squatId.toString()))
            .andExpect(jsonPath("$.exercises[1].exercise_id").value(benchPressId.toString()))
    }

    @Test
    fun `should fail when exercise does not exist`() {
        // Given: A non-existent exercise ID
        val nonExistentExerciseId = UUID.randomUUID()

        // When: Trying to log a workout with that exercise
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = nonExistentExerciseId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(setNumber = 1, weight = 100.0, reps = 10, timestamp = now.toEpochMilli())
                    )
                )
            )
        )

        // Then: Should return 400 Bad Request
        logWorkout(request, testUserId)
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Invalid exercise reference: $nonExistentExerciseId"))
    }

    @Test
    fun `should fail validation when exercises list is empty`() {
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = emptyList()
        )

        logWorkout(request, testUserId)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should fail validation when sets list is empty for completed exercise`() {
        val exerciseId = createExercise("Squat")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = emptyList()
                )
            )
        )

        logWorkout(request, testUserId)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should allow empty sets for skipped exercise`() {
        val exerciseId = createExercise("Squat")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "SKIPPED",
                    sets = emptyList()
                )
            )
        )

        logWorkout(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.exercises[0].status").value("SKIPPED"))
            .andExpect(jsonPath("$.exercises[0].sets.length()").value(0))
    }

    @Test
    fun `should allow empty sets for queued exercise`() {
        val exerciseId = createExercise("Squat")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "QUEUED",
                    sets = emptyList()
                )
            )
        )

        logWorkout(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.exercises[0].status").value("QUEUED"))
            .andExpect(jsonPath("$.exercises[0].sets.length()").value(0))
    }

    @Test
    fun `should allow empty sets for in_progress exercise`() {
        val exerciseId = createExercise("Squat")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "IN_PROGRESS",
                    sets = emptyList()
                )
            )
        )

        logWorkout(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.exercises[0].status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.exercises[0].sets.length()").value(0))
    }

    @Test
    fun `should allow mixed exercise statuses in same workout`() {
        val squatId = createExercise("Squat")
        val benchPressId = createExercise("Bench Press")
        val deadliftId = createExercise("Deadlift")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = squatId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(setNumber = 1, weight = 100.0, reps = 10, timestamp = now.toEpochMilli())
                    )
                ),
                WorkoutExerciseRequest(
                    exerciseId = benchPressId,
                    orderIndex = 1,
                    status = "SKIPPED",
                    sets = emptyList()
                ),
                WorkoutExerciseRequest(
                    exerciseId = deadliftId,
                    orderIndex = 2,
                    status = "QUEUED",
                    sets = emptyList()
                )
            )
        )

        logWorkout(request, testUserId)
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.exercises[0].status").value("COMPLETED"))
            .andExpect(jsonPath("$.exercises[0].sets.length()").value(1))
            .andExpect(jsonPath("$.exercises[1].status").value("SKIPPED"))
            .andExpect(jsonPath("$.exercises[1].sets.length()").value(0))
            .andExpect(jsonPath("$.exercises[2].status").value("QUEUED"))
            .andExpect(jsonPath("$.exercises[2].sets.length()").value(0))
    }

    @Test
    fun `should fail validation when weight is negative`() {
        val exerciseId = createExercise("Squat")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(setNumber = 1, weight = -10.0, reps = 10, timestamp = now.toEpochMilli())
                    )
                )
            )
        )

        logWorkout(request, testUserId)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should retrieve workout by id`() {
        // Given: A logged workout
        val exerciseId = createExercise("Squat")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(setNumber = 1, weight = 100.0, reps = 10, timestamp = now.toEpochMilli())
                    )
                )
            )
        )

        val workoutId = logWorkout(request, testUserId)
            .andReturn().response.contentAsString
            .let { objectMapper.readTree(it).get("id").asText() }
            .let { UUID.fromString(it) }

        // When: Retrieving the workout by ID
        getWorkout(workoutId, testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(workoutId.toString()))
            .andExpect(jsonPath("$.user_id").value(testUserId.toString()))
            .andExpect(jsonPath("$.status").value("COMPLETED"))
            .andExpect(jsonPath("$.exercises.length()").value(1))
    }

    @Test
    fun `should return 404 when workout does not exist`() {
        val nonExistentId = UUID.randomUUID()

        getWorkout(nonExistentId, testUserId)
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Workout not found"))
    }

    @Test
    fun `should list workouts for user`() {
        // Given: User has 3 workouts
        val exerciseId = createExercise("Squat")
        val now = Instant.now()

        repeat(3) { index ->
            val request = LogWorkoutRequest(
                startTime = now.minusSeconds(3600L * (index + 1)).toEpochMilli(),
                endTime = now.minusSeconds(3600L * index).toEpochMilli(),
                exercises = listOf(
                    WorkoutExerciseRequest(
                        exerciseId = exerciseId,
                        orderIndex = 0,
                        status = "COMPLETED",
                        sets = listOf(
                            WorkoutSetRequest(setNumber = 1, weight = 100.0, reps = 10, timestamp = now.toEpochMilli())
                        )
                    )
                )
            )
            logWorkout(request, testUserId)
        }

        // When: Listing workouts
        listWorkouts(testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].user_id").value(testUserId.toString()))
            .andExpect(jsonPath("$[0].status").value("COMPLETED"))
            .andExpect(jsonPath("$[0].total_sets").value(1))
            .andExpect(jsonPath("$[0].total_volume").value(1000.0))
            .andExpect(jsonPath("$[0].exercises.length()").value(1))
    }

    @Test
    fun `should return empty list when user has no workouts`() {
        listWorkouts(testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `should return all workouts for user`() {
        // Given: User has 25 workouts
        val exerciseId = createExercise("Squat")
        val now = Instant.now()

        repeat(25) { index ->
            val request = LogWorkoutRequest(
                startTime = now.minusSeconds(3600L * (index + 1)).toEpochMilli(),
                endTime = now.minusSeconds(3600L * index).toEpochMilli(),
                exercises = listOf(
                    WorkoutExerciseRequest(
                        exerciseId = exerciseId,
                        orderIndex = 0,
                        status = "COMPLETED",
                        sets = listOf(
                            WorkoutSetRequest(setNumber = 1, weight = 100.0, reps = 10, timestamp = now.toEpochMilli())
                        )
                    )
                )
            )
            logWorkout(request, testUserId)
        }

        // When: Requesting all workouts
        listWorkouts(testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(25))
    }

    @Test
    fun `should only show workouts for authenticated user`() {
        // Given: Two users with workouts
        val exerciseId = createExercise("Squat")
        val now = Instant.now()
        val request = LogWorkoutRequest(
            startTime = now.minusSeconds(3600).toEpochMilli(),
            endTime = now.toEpochMilli(),
            exercises = listOf(
                WorkoutExerciseRequest(
                    exerciseId = exerciseId,
                    orderIndex = 0,
                    status = "COMPLETED",
                    sets = listOf(
                        WorkoutSetRequest(setNumber = 1, weight = 100.0, reps = 10, timestamp = now.toEpochMilli())
                    )
                )
            )
        )

        logWorkout(request, testUserId)
        logWorkout(request, anotherUserId)

        // When: First user lists their workouts
        listWorkouts(testUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].user_id").value(testUserId.toString()))

        // When: Second user lists their workouts
        listWorkouts(anotherUserId)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].user_id").value(anotherUserId.toString()))
    }

    // Helper methods

    private fun createExercise(name: String): UUID {
        val response = postJson("/api/v1/exercises", CreateExerciseRequest(name = name))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        return objectMapper.readTree(response).get("id").asText().let { UUID.fromString(it) }
    }

    private fun logWorkout(request: LogWorkoutRequest, userId: UUID): ResultActions =
        postJsonAuthenticated("/api/v1/workouts", request, userId)

    private fun getWorkout(workoutId: UUID, userId: UUID): ResultActions =
        getJsonAuthenticated("/api/v1/workouts/$workoutId", userId)

    private fun listWorkouts(userId: UUID): ResultActions =
        getJsonAuthenticated("/api/v1/workouts", userId)
}
