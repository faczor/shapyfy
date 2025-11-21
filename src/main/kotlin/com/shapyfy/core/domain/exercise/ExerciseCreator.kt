package com.shapyfy.core.domain.exercise

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExerciseCreator(
    private val exerciseRepository: ExerciseRepository,
    private val translationPort: ExerciseTranslationPort
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun create(command: ExerciseCreationCommand): ExerciseDetails {
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

        // TODO: Once exercise library is built, these should come from user input or AI classification
        val exercise = exerciseRepository.saveNew(
            exercise = Exercise.new(
                name = canonicalName,
                primaryMuscleGroup = MuscleGroup.CHEST, // Temporary default
                secondaryMuscleGroups = emptySet(), // Temporary default
                equipmentRequired = setOf(Equipment.BODYWEIGHT), // Temporary default
                difficulty = Difficulty.BEGINNER, // Temporary default
                movementPattern = MovementPattern.ISOLATION // Temporary default
            ),
            language = command.preferredLanguage,
            detectedLanguage = canonicalization.detectedLanguage,
            confidence = canonicalization.confidence
        )

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
}
