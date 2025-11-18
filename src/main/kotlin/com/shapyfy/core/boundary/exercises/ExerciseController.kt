package com.shapyfy.core.boundary.exercises

import com.shapyfy.core.boundary.ApiController
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/exercises")
class ExerciseController(
    private val exerciseRestAdapter: ExerciseRestAdapter
): ApiController() {

    @PostMapping
    fun createExercise(
        @Valid @RequestBody request: CreateExerciseRequest,
        @RequestHeader(ACCEPT_LANGUAGE_HEADER, required = false) acceptLanguage: String?
    ): ResponseEntity<ExerciseResponse> {
        val response = exerciseRestAdapter.createExercise(request, acceptLanguage)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getExercises(
        @RequestHeader(ACCEPT_LANGUAGE_HEADER, required = false) acceptLanguage: String?
    ): ResponseEntity<GetExercisesResponse> {
        val exercises = exerciseRestAdapter.listExercises(acceptLanguage)
        return ResponseEntity.ok(GetExercisesResponse(exercises))
    }
}
