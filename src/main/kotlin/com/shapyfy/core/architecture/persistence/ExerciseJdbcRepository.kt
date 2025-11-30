package com.shapyfy.core.architecture.persistence

import com.shapyfy.core.architecture.metrics.MicrometerExerciseMetrics
import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.exercise.*
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository

@Repository
class ExerciseJdbcRepository(
    private val exerciseCrudRepository: ExerciseCrudRepository,
    private val exerciseMetrics: MicrometerExerciseMetrics
) : ExerciseRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun saveNew(
        exercise: Exercise,
        language: Language,
        detectedLanguage: Language,
        confidence: Double
    ): Exercise {
        log.info("Attempt to persist new exercise '{}' with id {}", exercise.canonicalName.value, exercise.id)
        val translationKey = TranslationKeyFactory.forExercise(exercise.canonicalName)
        val entity = ExerciseEntity.new(
            id = exercise.id.value,
            name = translationKey.value,
            primaryMuscleGroup = exercise.primaryMuscleGroup.name,
            secondaryMuscleGroups = exercise.secondaryMuscleGroups.map { it.name }.toTypedArray(),
            equipmentRequired = exercise.equipmentRequired.map { it.name }.toTypedArray(),
            difficulty = exercise.difficulty.name,
            movementPattern = exercise.movementPattern.name,
            createdAt = exercise.createdAt,
            updatedAt = exercise.updatedAt
        )

        try {
            exerciseCrudRepository.save(entity)
            log.info("Exercise '{}' persisted successfully", exercise.id)

            // Record metrics after successful persistence (architecture concern)
            exerciseMetrics.recordCreation(language, detectedLanguage, confidence)
            log.info(
                "Exercise creation metrics recorded for '{}' (lang: {}, detected: {}, confidence: {})",
                exercise.id, language.code, detectedLanguage.code, confidence
            )
        } catch (ex: DataAccessException) {
            log.error("Failed to persist exercise '{}' to database", exercise.id, ex)
            throw ex
        }
        return exercise
    }

    override fun findByName(name: String): Exercise? {
        log.info("Attempt to load exercise by canonical name '{}'", name)
        return try {
            exerciseCrudRepository.findByName(name)?.toDomain()
                ?.also { log.info("Exercise '{}' loaded for canonical name '{}'", it.id, name) }
        } catch (ex: DataAccessException) {
            log.error("Failed to load exercise by canonical name '{}'", name, ex)
            throw ex
        }
    }

    override fun findAll(): List<Exercise> {
        log.info("Attempt to load all exercises from database")
        return try {
            val result = exerciseCrudRepository.findAllBy().map { it.toDomain() }
            log.info("Loaded {} exercises from database", result.size)
            result
        } catch (ex: DataAccessException) {
            log.error("Failed to load exercises from database", ex)
            throw ex
        }
    }

    override fun existsById(id: ExerciseId): Boolean {
        log.info("Checking if exercise exists with id '{}'", id)
        return try {
            val exists = exerciseCrudRepository.existsById(id.value)
            log.info("Exercise '{}' exists: {}", id, exists)
            exists
        } catch (ex: DataAccessException) {
            log.error("Failed to check if exercise '{}' exists", id, ex)
            throw ex
        }
    }

    private fun ExerciseEntity.toDomain(): Exercise {
        // Extract canonical name from translation key (e.g., "exercises.squat" -> "squat")
        val canonicalName = if (name.startsWith("exercises.")) {
            name.substringAfter("exercises.")
        } else {
            // Fallback for legacy data or incorrect format
            name
        }
        return Exercise(
            id = ExerciseId.from(getId()),
            canonicalName = CanonicalExerciseName(canonicalName),
            primaryMuscleGroup = MuscleGroup.valueOf(primaryMuscleGroup),
            secondaryMuscleGroups = secondaryMuscleGroups.map { MuscleGroup.valueOf(it) }.toSet(),
            equipmentRequired = equipmentRequired.map { Equipment.valueOf(it) }.toSet(),
            difficulty = Difficulty.valueOf(difficulty),
            movementPattern = MovementPattern.valueOf(movementPattern),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
