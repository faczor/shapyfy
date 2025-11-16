package com.shapyfy.core.boundary

import com.shapyfy.core.domain.exercise.ExerciseDuplicateException
import com.shapyfy.core.domain.exercise.ExerciseMetricsPort
import com.shapyfy.core.domain.exercise.ExerciseTranslationPort
import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.exercise.TranslationCategory
import com.shapyfy.core.domain.plan.InvalidExerciseReferenceException as PlanInvalidExerciseReferenceException
import com.shapyfy.core.domain.plan.NoActivePlanException
import com.shapyfy.core.domain.plan.PlanNotFoundException
import com.shapyfy.core.domain.workout.InvalidExerciseReferenceException
import com.shapyfy.core.domain.workout.WorkoutNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler(
    private val translationPort: ExerciseTranslationPort,
    private val exerciseMetrics: ExerciseMetricsPort
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(SyncException::class)
    fun handleSyncException(exception: SyncException): ResponseEntity<SyncErrorResponse> =
        exception.also {
            log.error("Sync exception handled: {}", it.message, it)
        }.let {
            ResponseEntity
                .status(it.status)
                .body(it.toResponse())
        }

    @ExceptionHandler(ExerciseDuplicateException::class)
    fun handleExerciseDuplicate(exception: ExerciseDuplicateException): ResponseEntity<SyncErrorResponse> {
        log.error("Duplicate exercise detected: {}", exception.message, exception)
        // Record conflict metric at architecture layer
        exerciseMetrics.recordConflict(Language.EN)

        val exercise = exception.exercise

        // Get all translations for conflict response
        val allTranslations = Language.entries.associate { lang ->
            lang.code to translationPort.localize(exercise, lang)
        }

        val syncException = SyncException(
            status = HttpStatus.CONFLICT,
            id = exercise.id.value,
            translationKey = "${TranslationCategory.EXERCISES.value}.${exercise.name}",
            availableTranslations = allTranslations,
            errorMessage = "Exercise with the same name already exists"
        )

        return ResponseEntity
            .status(syncException.status)
            .body(syncException.toResponse())
    }

    @ExceptionHandler(InvalidExerciseReferenceException::class)
    fun handleInvalidExerciseReference(exception: InvalidExerciseReferenceException): ResponseEntity<ErrorResponse> {
        log.error("Invalid exercise reference: {}", exception.message, exception)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    message = "Invalid exercise reference: ${exception.exerciseId.value}",
                    details = "The exercise ID '${exception.exerciseId.value}' does not exist"
                )
            )
    }

    @ExceptionHandler(WorkoutNotFoundException::class)
    fun handleWorkoutNotFound(exception: WorkoutNotFoundException): ResponseEntity<ErrorResponse> {
        log.error("Workout not found: {}", exception.message, exception)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = "Workout not found",
                    details = "The workout with ID '${exception.workoutId.value}' does not exist"
                )
            )
    }

    @ExceptionHandler(PlanNotFoundException::class)
    fun handlePlanNotFound(exception: PlanNotFoundException): ResponseEntity<ErrorResponse> {
        log.error("Plan not found: {}", exception.message, exception)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = "Plan not found",
                    details = "The plan with ID '${exception.planId.value}' does not exist"
                )
            )
    }

    @ExceptionHandler(PlanInvalidExerciseReferenceException::class)
    fun handlePlanInvalidExerciseReference(exception: PlanInvalidExerciseReferenceException): ResponseEntity<ErrorResponse> {
        log.error("Invalid exercise reference in plan: {}", exception.message, exception)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    message = "Invalid exercise reference in plan",
                    details = "The exercise ID '${exception.exerciseId.value}' does not exist"
                )
            )
    }

    @ExceptionHandler(NoActivePlanException::class)
    fun handleNoActivePlan(exception: NoActivePlanException): ResponseEntity<ErrorResponse> {
        log.error("No active plan found: {}", exception.message, exception)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = "No active plan found",
                    details = "User does not have an active plan"
                )
            )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(exception: DataAccessException): ResponseEntity<ErrorResponse> {
        log.error("Database operation failed: {}", exception.message, exception)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    message = "Database operation failed",
                    details = "An error occurred while accessing the database. Please try again later."
                )
            )
    }
}

/**
 * Generic error response
 */
data class ErrorResponse(
    val message: String,
    val details: String? = null
)
