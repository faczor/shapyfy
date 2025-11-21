package com.shapyfy.core.architecture.metrics

import com.shapyfy.core.domain.Language
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

/**
 * Architecture-level component for recording exercise metrics.
 * Used directly by architecture adapters (repositories, exception handlers).
 */
@Component
class MicrometerExerciseMetrics(
    private val meterRegistry: MeterRegistry
) {

    private val confidenceSummary = meterRegistry.summary("exercise_ai_confidence")

    fun recordCreation(language: Language, detectedLanguage: Language, confidence: Double) {
        meterRegistry.counter(
            "exercise_created_total",
            "language", language.code,
            "detected_language", detectedLanguage.code
        ).increment()

        confidenceSummary.record(confidence)
    }

    fun recordConflict(language: Language) {
        meterRegistry.counter(
            "exercise_conflict_total",
            "language", language.code
        ).increment()
    }
}
