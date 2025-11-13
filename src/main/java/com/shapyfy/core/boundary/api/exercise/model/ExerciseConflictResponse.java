package com.shapyfy.core.boundary.api.exercise.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Response returned when exercise creation fails due to conflict (duplicate).
 * Includes the ID of the existing exercise that conflicts.
 */
public record ExerciseConflictResponse(
        @JsonProperty("status") int status,
        @JsonProperty("message") String message,
        @JsonProperty("conflictingResourceId") UUID conflictingResourceId,
        @JsonProperty("conflictType") String conflictType,
        @JsonProperty("existingExerciseName") String existingExerciseName,
        @JsonProperty("matchedTranslation") String matchedTranslation
) {
    public static ExerciseConflictResponse of(UUID conflictingResourceId, String existingExerciseName, String matchedTranslation) {
        return new ExerciseConflictResponse(
                409,
                "Exercise already exists",
                conflictingResourceId,
                "DUPLICATE_EXERCISE",
                existingExerciseName,
                matchedTranslation
        );
    }
}
