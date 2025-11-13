package com.shapyfy.core.boundary.api.exercise.adapter;

import com.shapyfy.core.boundary.api.exercise.model.CreateExerciseRequest;
import com.shapyfy.core.boundary.api.exercise.model.ExerciseContract;
import com.shapyfy.core.domain.Exercises;
import com.shapyfy.core.domain.TranslationService;
import com.shapyfy.core.domain.model.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExerciseApiAdapter {

    private final Exercises exercises;
    private final TranslationService translationService;

    public Exercise createExercise(CreateExerciseRequest request, String language) {
        log.info("Creating exercise {} for language {}", request, language);
        return exercises.create(request.name());
    }

    /**
     * Create exercise with deduplication support.
     * Returns resolution result to handle conflicts.
     */
    public com.shapyfy.core.domain.model.ExerciseResolutionResult createExerciseWithDeduplication(
            CreateExerciseRequest request,
            String language
    ) {
        log.info("Creating exercise with deduplication: {} for language {}", request, language);
        return exercises.createWithDeduplication(request.name(), language);
    }

    public ExerciseContract fetchExercise(UUID exerciseId, String language) {
        log.info("Fetching exercise {} for language {}", exerciseId, language);
        Exercise exercise = exercises.fetchById(Exercise.ExerciseId.of(exerciseId));

        return toContract(exercise, language);
    }

    public List<ExerciseContract> fetchExercises(String language) {
        log.info("Fetching all exercises for language {}", language);
        List<Exercise> fetchedExercises = exercises.fetchAll();

        return toContracts(fetchedExercises, language);
    }

    /**
     * Convert single Exercise to ExerciseContract with translation
     */
    private ExerciseContract toContract(Exercise exercise, String language) {
        String translatedName = translationService.translate(exercise.getTranslationKey(), language);
        return new ExerciseContract(exercise.getId().getId(), translatedName);
    }

    /**
     * Convert list of Exercises to ExerciseContracts with batch translation.
     * More efficient than translating one-by-one.
     */
    private List<ExerciseContract> toContracts(List<Exercise> exercises, String language) {
        if (exercises.isEmpty()) {
            return List.of();
        }

        // Collect all translation keys
        List<String> translationKeys = exercises.stream()
                .map(Exercise::getTranslationKey)
                .toList();

        // Batch translate - single cache lookup!
        Map<String, String> translations = translationService.translateBatch(translationKeys, language);

        // Map to contracts
        return exercises.stream()
                .map(ex -> new ExerciseContract(
                        ex.getId().getId(),
                        translations.get(ex.getTranslationKey())
                ))
                .toList();
    }
}
