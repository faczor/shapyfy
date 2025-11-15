package com.shapyfy.core.domain.workout

import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.WorkoutId
import org.springframework.stereotype.Service

/**
 * Domain service responsible for fetching workout data.
 */
@Service
class WorkoutFetcher(
    private val workoutRepository: WorkoutRepository
) {

    fun getWorkoutById(id: WorkoutId): Workout {
        return workoutRepository.findById(id) ?: throw WorkoutNotFoundException(id)
    }

    fun getAllWorkoutsForUser(userId: UserId): List<Workout> {
        return workoutRepository.findAllByUserId(userId)
    }
}