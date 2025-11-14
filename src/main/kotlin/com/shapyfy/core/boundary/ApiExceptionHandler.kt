package com.shapyfy.core.boundary

import com.shapyfy.core.domain.ExerciseDuplicateException
import com.shapyfy.core.domain.ExerciseMetricsPort
import com.shapyfy.core.domain.ExerciseTranslationPort
import com.shapyfy.core.domain.model.Language
import com.shapyfy.core.domain.model.TranslationCategory
import org.slf4j.LoggerFactory
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
            id = exercise.id,
            translationKey = "${TranslationCategory.EXERCISES.value}.${exercise.name}",
            availableTranslations = allTranslations,
            errorMessage = "Exercise with the same name already exists"
        )

        return ResponseEntity
            .status(syncException.status)
            .body(syncException.toResponse())
    }
}
