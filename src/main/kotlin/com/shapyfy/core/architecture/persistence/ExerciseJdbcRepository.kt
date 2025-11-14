package com.shapyfy.core.architecture.persistence

import com.shapyfy.core.domain.model.Exercise
import com.shapyfy.core.domain.ExerciseRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository

@Repository
class ExerciseJdbcRepository(
    private val exerciseCrudRepository: ExerciseCrudRepository
) : ExerciseRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(exercise: Exercise): Exercise {
        log.info("Attempt to persist exercise '{}' with id {}", exercise.name, exercise.id)
        val entity = ExerciseEntity.new(
            id = exercise.id,
            name = exercise.name,
            createdAt = exercise.createdAt,
            updatedAt = exercise.updatedAt
        )

        try {
            exerciseCrudRepository.save(entity)
            log.info("Exercise '{}' persisted successfully", exercise.id)
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

    private fun ExerciseEntity.toDomain(): Exercise =
        Exercise(
            id = getId(),
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
}
