package com.shapyfy.core.domain.exercise

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExerciseCreator(
    private val exerciseRepository: ExerciseRepository,
    private val translationPort: ExerciseTranslationPort,
    private val classificationPort: ExerciseClassificationPort
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun create(command: ExerciseCreationCommand): ExerciseDetails {
        log.info("Attempt to create domain exercise with raw name '{}'", command.rawName)

        val canonicalization = translationPort.canonicalize(command.rawName, command.preferredLanguage)
        val canonicalName = canonicalization.canonicalName
        val translationKey = TranslationKeyFactory.forExercise(canonicalName)

        throwDuplicateIfIsAlreadyExisting(translationKey, canonicalName)

        log.info("Classifying exercise properties for '{}'", canonicalName)
        val properties = classificationPort.classify(canonicalName)

        val exercise = exerciseRepository.saveNew(
            exercise = Exercise.new(
                canonicalName = CanonicalExerciseName(canonicalName),
                primaryMuscleGroup = properties.primaryMuscleGroup,
                secondaryMuscleGroups = properties.secondaryMuscleGroups,
                equipmentRequired = properties.equipmentRequired,
                difficulty = properties.difficulty,
                movementPattern = properties.movementPattern
            ),
            language = command.preferredLanguage,
            detectedLanguage = canonicalization.detectedLanguage,
            confidence = canonicalization.confidence
        )

        val localizedName = translationPort.storeTranslations(
            exercise = exercise,
            rawName = command.rawName,
            preferredLanguage = command.preferredLanguage,
            detectedLanguage = canonicalization.detectedLanguage
        )

        log.info("Exercise '{}' created with id {}", canonicalName, exercise.id)
        return ExerciseDetails(
            id = exercise.id,
            localizedName = localizedName,
            translationKey = translationKey,
            primaryMuscleGroup = exercise.primaryMuscleGroup,
            secondaryMuscleGroups = exercise.secondaryMuscleGroups,
            equipmentRequired = exercise.equipmentRequired,
            difficulty = exercise.difficulty,
            movementPattern = exercise.movementPattern
        )
    }

    private fun throwDuplicateIfIsAlreadyExisting(translationKey: TranslationKey, canonicalName: String) {
        val existing = exerciseRepository.findByName(translationKey.value)
        if (existing != null) {
            log.error("Duplicate exercise detected for canonical name '{}'", canonicalName)
            throw ExerciseDuplicateException(existing)
        }
    }
}
