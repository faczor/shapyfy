package com.shapyfy.core.architecture.persistence

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.PlanDayId
import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.WorkoutExerciseId
import com.shapyfy.core.domain.WorkoutId
import com.shapyfy.core.domain.WorkoutSetId
import com.shapyfy.core.domain.workout.ExerciseStatus
import com.shapyfy.core.domain.workout.Workout
import com.shapyfy.core.domain.workout.WorkoutExercise
import com.shapyfy.core.domain.workout.WorkoutRepository
import com.shapyfy.core.domain.workout.WorkoutSet
import com.shapyfy.core.domain.workout.WorkoutStatus
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class WorkoutJdbcRepository(
    private val workoutCrudRepository: WorkoutCrudRepository,
    private val workoutExerciseCrudRepository: WorkoutExerciseCrudRepository,
    private val workoutSetCrudRepository: WorkoutSetCrudRepository
) : WorkoutRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun save(workout: Workout): Workout {
        log.info("Attempting to persist workout '{}' for user '{}'", workout.id, workout.userId)

        try {
            // Save workout entity
            val workoutEntity = WorkoutEntity.new(
                id = workout.id.value,
                userId = workout.userId.value,
                status = workout.status.name,
                startTime = workout.startTime,
                endTime = workout.endTime,
                planDayId = workout.planDayId?.value,
                createdAt = workout.createdAt,
                updatedAt = workout.updatedAt
            )
            workoutCrudRepository.save(workoutEntity)

            // Save workout exercises
            workout.exercises.forEach { exercise ->
                val exerciseEntity = WorkoutExerciseEntity.new(
                    id = exercise.id.value,
                    workoutId = workout.id.value,
                    exerciseId = exercise.exerciseId.value,
                    orderIndex = exercise.orderIndex,
                    status = exercise.status.name
                )
                workoutExerciseCrudRepository.save(exerciseEntity)

                // Save sets for this exercise
                exercise.sets.forEach { set ->
                    val setEntity = WorkoutSetEntity.new(
                        id = set.id.value,
                        workoutExerciseId = exercise.id.value,
                        setNumber = set.setNumber,
                        weight = set.weight,
                        reps = set.reps,
                        timestamp = set.timestamp
                    )
                    workoutSetCrudRepository.save(setEntity)
                }
            }

            log.info("Workout '{}' persisted successfully with {} exercises and {} total sets",
                workout.id, workout.exercises.size, workout.totalSets())
            return workout
        } catch (ex: DataAccessException) {
            log.error("Failed to persist workout '{}' to database", workout.id, ex)
            throw ex
        }
    }

    override fun findById(id: WorkoutId): Workout? {
        log.info("Attempting to load workout by id '{}'", id)

        return try {
            val workoutEntity = workoutCrudRepository.findById(id.value).orElse(null)
                ?: return null.also { log.info("Workout '{}' not found", id) }

            val exerciseEntities = workoutExerciseCrudRepository.findByWorkoutId(id.value)
            val exerciseIds = exerciseEntities.map { it.getId() }
            val setEntities = workoutSetCrudRepository.findByWorkoutExerciseIdIn(exerciseIds)

            val setsByExerciseId = setEntities.groupBy { it.workoutExerciseId }

            val exercises = exerciseEntities.map { exerciseEntity ->
                val sets = setsByExerciseId[exerciseEntity.getId()]
                    ?.map { it.toDomain() }
                    ?.sortedBy { it.setNumber }
                    ?: emptyList()

                WorkoutExercise(
                    id = WorkoutExerciseId.from(exerciseEntity.getId()),
                    exerciseId = ExerciseId.from(exerciseEntity.exerciseId),
                    orderIndex = exerciseEntity.orderIndex,
                    status = ExerciseStatus.valueOf(exerciseEntity.status),
                    sets = sets
                )
            }.sortedBy { it.orderIndex }

            val workout = workoutEntity.toDomain(exercises)
            log.info("Workout '{}' loaded successfully", id)
            workout
        } catch (ex: DataAccessException) {
            log.error("Failed to load workout '{}' from database", id, ex)
            throw ex
        }
    }

    override fun findAllByUserId(userId: UserId): List<Workout> {
        log.info("Attempting to load all workouts for user '{}'", userId)

        return try {
            val workoutEntities = workoutCrudRepository.findAllByUserId(userId.value)
            val workoutIds = workoutEntities.map { it.getId() }

            if (workoutIds.isEmpty()) {
                return emptyList<Workout>()
                    .also { log.info("No workouts found for user '{}'", userId) }
            }

            // Load all exercises for these workouts
            val allExerciseEntities = workoutIds.flatMap { workoutId ->
                workoutExerciseCrudRepository.findByWorkoutId(workoutId)
            }

            val allExerciseIds = allExerciseEntities.map { it.getId() }
            val allSetEntities = workoutSetCrudRepository.findByWorkoutExerciseIdIn(allExerciseIds)

            // Group sets by workout exercise ID
            val setsByExerciseId = allSetEntities.groupBy { it.workoutExerciseId }

            // Group exercises by workout ID
            val exercisesByWorkoutId = allExerciseEntities.groupBy { it.workoutId }

            val workouts = workoutEntities.map { workoutEntity ->
                val workoutId = workoutEntity.getId()
                val exerciseEntities = exercisesByWorkoutId[workoutId] ?: emptyList()

                val exercises = exerciseEntities.map { exerciseEntity ->
                    val sets = setsByExerciseId[exerciseEntity.getId()]
                        ?.map { it.toDomain() }
                        ?.sortedBy { it.setNumber }
                        ?: emptyList()

                    WorkoutExercise(
                        id = WorkoutExerciseId.from(exerciseEntity.getId()),
                        exerciseId = ExerciseId.from(exerciseEntity.exerciseId),
                        orderIndex = exerciseEntity.orderIndex,
                        status = ExerciseStatus.valueOf(exerciseEntity.status),
                        sets = sets
                    )
                }.sortedBy { it.orderIndex }

                workoutEntity.toDomain(exercises)
            }

            log.info("Loaded {} workouts for user '{}'", workouts.size, userId)
            workouts
        } catch (ex: DataAccessException) {
            log.error("Failed to load workouts for user '{}'", userId, ex)
            throw ex
        }
    }

    private fun WorkoutEntity.toDomain(exercises: List<WorkoutExercise>): Workout =
        Workout(
            id = WorkoutId.from(getId()),
            userId = UserId.from(userId),
            status = WorkoutStatus.valueOf(status),
            startTime = startTime,
            endTime = endTime,
            exercises = exercises,
            planDayId = planDayId?.let { PlanDayId.from(it) },
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    private fun WorkoutSetEntity.toDomain(): WorkoutSet =
        WorkoutSet(
            id = WorkoutSetId.from(getId()),
            setNumber = setNumber,
            weight = weight,
            reps = reps,
            timestamp = timestamp
        )
}
