package com.shapyfy.core.domain.exercise

import com.shapyfy.core.domain.Language
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExerciseFetcher(
    private val exerciseRepository: ExerciseRepository,
    private val translationPort: ExerciseTranslationPort
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun fetchAll(language: Language): List<ExerciseSummary> {
        log.info("Attempt to fetch all domain exercises for language {}", language.code)
        val exercises = exerciseRepository.findAll()
        log.info("Fetched {} exercises from repository", exercises.size)

        return exercises.map { exercise ->
            val localizedName = translationPort.localize(exercise, language)
            ExerciseSummary(
                id = exercise.id,
                localizedName = localizedName,
                translationKey = TranslationKeyFactory.forExercise(exercise.name)
            )
        }
    }
}
