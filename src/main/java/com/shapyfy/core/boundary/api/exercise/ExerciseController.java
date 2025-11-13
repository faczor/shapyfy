package com.shapyfy.core.boundary.api.exercise;

import com.shapyfy.core.boundary.api.exercise.adapter.ExerciseApiAdapter;
import com.shapyfy.core.boundary.api.exercise.model.CreateExerciseRequest;
import com.shapyfy.core.boundary.api.exercise.model.ExerciseContract;
import com.shapyfy.core.boundary.api.exercise.model.GetExercisesResponse;
import com.shapyfy.core.domain.model.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseApiAdapter exerciseApiAdapter;

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ExerciseContract> getExercise(
            @PathVariable(name = "exerciseId") UUID exerciseId,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguage
    ) {
        String language = parseLanguage(acceptLanguage);
        log.debug("Fetching exercise with id: {} for language: {}", exerciseId, language);
        return ResponseEntity.ok(exerciseApiAdapter.fetchExercise(exerciseId, language));
    }

    @GetMapping
    public ResponseEntity<GetExercisesResponse> getExercises(
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguage
    ) {
        String language = parseLanguage(acceptLanguage);
        log.debug("Fetching all exercises for language: {}", language);
        return ResponseEntity.ok(new GetExercisesResponse(exerciseApiAdapter.fetchExercises(language)));
    }

    @PostMapping
    public ResponseEntity<?> createExercise(
            @RequestBody CreateExerciseRequest request,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguage
    ) {
        String language = parseLanguage(acceptLanguage);
        log.info("Creating exercise: {} for language: {}", request, language);

        var resolution = exerciseApiAdapter.createExerciseWithDeduplication(request, language);

        if (resolution.isConflict()) {
            log.warn("Exercise creation conflict for name: {}", request.name());
            var existingExercise = resolution.existingExercise().orElseThrow();
            var conflictResponse = com.shapyfy.core.boundary.api.exercise.model.ExerciseConflictResponse.of(
                    existingExercise.getId().getId(),
                    resolution.languageDetection().englishName(),
                    resolution.matchedTranslation()
            );
            return ResponseEntity.status(409).body(conflictResponse);
        }

        log.info("Exercise created successfully with translation key: {}", resolution.canonicalTranslationKey());

        // Return the created exercise with translated name
        Exercise createdExercise = resolution.exercise().orElseThrow();
        ExerciseContract exerciseContract = exerciseApiAdapter.fetchExercise(
                createdExercise.getId().getId(),
                language
        );

        return ResponseEntity.ok(exerciseContract);
    }

    /**
     * Parse Accept-Language header to extract primary language code.
     * Examples:
     * - "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7" → "pl"
     * - "en-US" → "en"
     * - "pl" → "pl"
     */
    private String parseLanguage(String acceptLanguage) {
        if (acceptLanguage == null || acceptLanguage.isBlank()) {
            return "en";
        }

        // Take first language from comma-separated list
        String firstLanguage = acceptLanguage.split(",")[0].trim();

        // Extract language code before hyphen or semicolon
        String languageCode = firstLanguage.split("[-;]")[0].toLowerCase();

        log.debug("Parsed language '{}' from Accept-Language header: {}", languageCode, acceptLanguage);

        return languageCode;
    }
}
