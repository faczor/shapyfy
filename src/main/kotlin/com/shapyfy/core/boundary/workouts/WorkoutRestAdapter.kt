package com.shapyfy.core.boundary.workouts

import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.WorkoutId
import com.shapyfy.core.domain.workout.ExerciseLoggingData
import com.shapyfy.core.domain.workout.ExerciseStatus
import com.shapyfy.core.domain.workout.SetLoggingData
import com.shapyfy.core.domain.workout.Workout
import com.shapyfy.core.domain.workout.WorkoutExercise
import com.shapyfy.core.domain.workout.WorkoutFetcher
import com.shapyfy.core.domain.workout.WorkoutLogger
import com.shapyfy.core.domain.workout.WorkoutLoggingCommand
import com.shapyfy.core.domain.workout.WorkoutSet
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class WorkoutRestAdapter(
    private val workoutLogger: WorkoutLogger,
    private val workoutFetcher: WorkoutFetcher
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun logWorkout(request: LogWorkoutRequest, userId: UserId): WorkoutResponse {
        log.info("Attempting to log workout for user '{}' with {} exercises", userId, request.exercises.size)

        val command = WorkoutLoggingCommand(
            userId = userId,
            startTime = Instant.ofEpochMilli(request.startTime),
            endTime = Instant.ofEpochMilli(request.endTime),
            exercises = request.exercises.map { exerciseRequest ->
                ExerciseLoggingData(
                    exerciseId = ExerciseId.from(exerciseRequest.exerciseId),
                    orderIndex = exerciseRequest.orderIndex,
                    status = ExerciseStatus.valueOf(exerciseRequest.status.uppercase()),
                    sets = exerciseRequest.sets.map { setRequest ->
                        SetLoggingData(
                            setNumber = setRequest.setNumber,
                            weight = setRequest.weight,
                            reps = setRequest.reps,
                            timestamp = Instant.ofEpochMilli(setRequest.timestamp)
                        )
                    }
                )
            }
        )

        val workout = workoutLogger.logWorkout(command)
        val response = workout.toResponse()

        log.info("Workout '{}' logged successfully for user '{}'", workout.id, userId)
        return response
    }

    fun getWorkoutById(id: WorkoutId): WorkoutResponse {
        log.info("Attempting to fetch workout '{}'", id)
        val workout = workoutFetcher.getWorkoutById(id)
        log.info("Workout '{}' fetched successfully", id)
        return workout.toResponse()
    }

    fun getAllWorkoutsForUser(userId: UserId): List<WorkoutResponse> {
        log.info("Attempting to fetch all workouts for user '{}'", userId)
        val workouts = workoutFetcher.getAllWorkoutsForUser(userId)
        val response = workouts.map { it.toResponse() }
        log.info("Returning {} workouts for user '{}'", response.size, userId)
        return response
    }

    private fun Workout.toResponse(): WorkoutResponse = WorkoutResponse(
        id = id.value,
        userId = userId.value,
        status = status.name,
        startTime = startTime.toEpochMilli(),
        endTime = endTime.toEpochMilli(),
        totalSets = totalSets(),
        totalVolume = totalVolume(),
        exercises = exercises.map { it.toResponse() }
    )

    private fun WorkoutExercise.toResponse(): WorkoutExerciseResponse = WorkoutExerciseResponse(
        id = id.value,
        exerciseId = exerciseId.value,
        orderIndex = orderIndex,
        status = status.name,
        sets = sets.map { it.toResponse() }
    )

    private fun WorkoutSet.toResponse(): WorkoutSetResponse = WorkoutSetResponse(
        id = id.value,
        setNumber = setNumber,
        weight = weight,
        reps = reps,
        timestamp = timestamp.toEpochMilli()
    )

}
