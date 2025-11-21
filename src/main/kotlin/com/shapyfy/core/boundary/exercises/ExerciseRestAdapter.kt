package com.shapyfy.core.boundary.exercises

import com.shapyfy.core.domain.exercise.ExerciseCreator
import com.shapyfy.core.domain.exercise.ExerciseFetcher
import com.shapyfy.core.domain.exercise.ExerciseCreationCommand
import com.shapyfy.core.domain.Language
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory

@Component
class ExerciseRestAdapter(
    private val exerciseCreator: ExerciseCreator,
    private val exerciseFetcher: ExerciseFetcher
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun createExercise(
        request: CreateExerciseRequest,
        acceptLanguage: String?
    ): ExerciseResponse {
        log.info(
            "Attempt to create exercise with name '{}' (accept-language={})",
            request.name,
            acceptLanguage
        )
        val language = Language.from(acceptLanguage)

        val result = exerciseCreator.create(
            ExerciseCreationCommand(
                rawName = request.name,
                preferredLanguage = language
            )
        )

        val response = ExerciseResponse.from(result);
        log.info(
            "Returning exercise creation response: id={}, translationKey={}",
            response.id,
            response.translationKey
        )
        return response
    }

    fun listExercises(acceptLanguage: String?): List<ExerciseResponse> {
        log.info("Attempt to list exercises (accept-language={})", acceptLanguage)
        val language = Language.from(acceptLanguage)
        val exercises = exerciseFetcher.fetchAll(language)

        val responses = exercises.map { ExerciseResponse.from(it) }
        log.info(
            "Returning {} exercise records (accept-language={})",
            responses.size,
            acceptLanguage
        )
        return responses
    }

}
