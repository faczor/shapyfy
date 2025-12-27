package com.shapyfy.core.boundary.workouts

import com.shapyfy.core.architecture.config.JwtToken
import com.shapyfy.core.boundary.ApiController
import com.shapyfy.core.domain.WorkoutId
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/workouts")
class WorkoutController(
    private val workoutRestAdapter: WorkoutRestAdapter
) : ApiController() {

    @PostMapping
    fun logWorkout(
        @Valid @RequestBody request: LogWorkoutRequest,
        jwt: JwtToken
    ): ResponseEntity<WorkoutResponse> {
        val response = workoutRestAdapter.logWorkout(request, jwt.userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getWorkout(
        @PathVariable id: UUID,
        jwt: JwtToken
    ): ResponseEntity<WorkoutResponse> {
        // TODO: Add authorization check - ensure user can only access their own workouts
        val response = workoutRestAdapter.getWorkoutById(WorkoutId.from(id))
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getWorkouts(
        jwt: JwtToken
    ): ResponseEntity<List<WorkoutResponse>> {
        val response = workoutRestAdapter.getAllWorkoutsForUser(jwt.userId)

        return ResponseEntity.ok(response)
    }
}
