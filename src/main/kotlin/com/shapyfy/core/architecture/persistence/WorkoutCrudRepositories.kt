package com.shapyfy.core.architecture.persistence

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface WorkoutCrudRepository : CrudRepository<WorkoutEntity, UUID> {
    @Query(
        """
        SELECT id, user_id, status, start_time, end_time, created_at, updated_at
        FROM workouts
        WHERE user_id = :userId
        ORDER BY start_time DESC
        """
    )
    fun findAllByUserId(@Param("userId") userId: String): List<WorkoutEntity>
}

interface WorkoutExerciseCrudRepository : CrudRepository<WorkoutExerciseEntity, UUID> {
    fun findByWorkoutId(workoutId: UUID): List<WorkoutExerciseEntity>
    fun deleteByWorkoutId(workoutId: UUID)
}

interface WorkoutSetCrudRepository : CrudRepository<WorkoutSetEntity, UUID> {
    fun findByWorkoutExerciseId(workoutExerciseId: UUID): List<WorkoutSetEntity>
    fun findByWorkoutExerciseIdIn(workoutExerciseIds: List<UUID>): List<WorkoutSetEntity>
}
