package com.shapyfy.core.domain

import com.shapyfy.core.domain.model.Exercise
import com.shapyfy.core.domain.model.ExerciseCreationCommand
import com.shapyfy.core.domain.model.ExerciseCreationResult
import com.shapyfy.core.domain.model.TranslationKeyFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExerciseCreator(
    private val exerciseRepository: ExerciseRepository,
    private val metricsPort: ExerciseMetricsPort,
    private val translationPort: ExerciseTranslationPort
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun create(command: ExerciseCreationCommand): ExerciseCreationResult {
        log.info("Attempt to create domain exercise with raw name '{}'", command.rawName)

        val canonicalization = translationPort.canonicalize(command.rawName, command.preferredLanguage)
        if (canonicalization.detectedLanguage != command.preferredLanguage) {
            log.error(
                "Provided language {} mismatch with detected {} for exercise name '{}'",
                command.preferredLanguage.code,
                canonicalization.detectedLanguage.code,
                command.rawName
            )
        }
        val canonicalName = canonicalization.canonicalName

        val existing = exerciseRepository.findByName(canonicalName)

        if (existing != null) {
            log.error("Duplicate exercise detected for canonical name '{}'", canonicalName)
            throw ExerciseDuplicateException(existing)
        }

        val exercise = exerciseRepository.save(Exercise.new(canonicalName))
        metricsPort.recordCreation(command.preferredLanguage, canonicalization.detectedLanguage, canonicalization.confidence)

        if (!canonicalization.isExistingTranslation) {
            translationPort.storeTranslations(
                exercise = exercise,
                rawName = command.rawName,
                preferredLanguage = command.preferredLanguage,
                detectedLanguage = canonicalization.detectedLanguage
            )
        }

        val localizedName = translationPort.localize(exercise, command.preferredLanguage)
        val translationKey = TranslationKeyFactory.forExercise(exercise.name)

        log.info("Exercise '{}' created with id {}", canonicalName, exercise.id)
        return ExerciseCreationResult(
            id = exercise.id,
            localizedName = localizedName,
            translationKey = translationKey
        )
    }
}
