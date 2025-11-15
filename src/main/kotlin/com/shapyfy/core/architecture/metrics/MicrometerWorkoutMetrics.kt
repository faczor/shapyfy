package com.shapyfy.core.architecture.metrics

import com.shapyfy.core.domain.workout.WorkoutMetricsPort
import com.shapyfy.core.domain.UserId
import com.shapyfy.core.domain.ExerciseId
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class MicrometerWorkoutMetrics(
    private val meterRegistry: MeterRegistry
) : WorkoutMetricsPort {

    override fun recordWorkoutLogged(userId: UserId, totalSets: Int, totalVolume: Double) {
        meterRegistry.counter("workout_logged_total").increment()
        meterRegistry.summary("workout_sets").record(totalSets.toDouble())
        meterRegistry.summary("workout_volume_kg").record(totalVolume)
    }

    override fun recordInvalidExerciseReference(exerciseId: ExerciseId) {
        meterRegistry.counter("workout_invalid_exercise_reference_total").increment()
    }
}
