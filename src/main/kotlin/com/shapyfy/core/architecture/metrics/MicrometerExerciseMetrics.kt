package com.shapyfy.core.architecture.metrics

import com.shapyfy.core.domain.model.Language
import com.shapyfy.core.domain.ExerciseMetricsPort
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class MicrometerExerciseMetrics(
    private val meterRegistry: MeterRegistry
) : ExerciseMetricsPort {

    private val confidenceSummary = meterRegistry.summary("exercise_ai_confidence")

    override fun recordCreation(language: Language, detectedLanguage: Language, confidence: Double) {
        meterRegistry.counter(
            "exercise_created_total",
            "language", language.code,
            "detected_language", detectedLanguage.code
        ).increment()

        confidenceSummary.record(confidence)
    }

    override fun recordConflict(language: Language) {
        meterRegistry.counter(
            "exercise_conflict_total",
            "language", language.code
        ).increment()
    }
}
