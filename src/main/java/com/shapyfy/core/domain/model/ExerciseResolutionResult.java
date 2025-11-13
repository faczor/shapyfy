package com.shapyfy.core.domain.model;

import java.util.Optional;

/**
 * Result of exercise deduplication resolution.
 * Indicates whether exercise already exists (conflict) or was successfully created.
 */
public record ExerciseResolutionResult(
        boolean isConflict,
        Optional<Exercise> exercise,
        LanguageDetection languageDetection,
        String canonicalTranslationKey,
        String matchedTranslation
) {

    public static ExerciseResolutionResult conflict(Exercise existingExercise, LanguageDetection detection, String matchedTranslation) {
        return new ExerciseResolutionResult(
                true,
                Optional.of(existingExercise),
                detection,
                existingExercise.getTranslationKey(),
                matchedTranslation
        );
    }

    public static ExerciseResolutionResult success(Exercise createdExercise, LanguageDetection detection) {
        return new ExerciseResolutionResult(
                false,
                Optional.of(createdExercise),
                detection,
                createdExercise.getTranslationKey(),
                null
        );
    }

    public static ExerciseResolutionResult allowCreation(LanguageDetection detection, String translationKey) {
        return new ExerciseResolutionResult(
                false,
                Optional.empty(),
                detection,
                translationKey,
                null
        );
    }

    /**
     * Get exercise (either existing or newly created).
     */
    public Optional<Exercise> existingExercise() {
        return exercise;
    }
}
