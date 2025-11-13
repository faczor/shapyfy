package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.Exercise;
import com.shapyfy.core.domain.model.ExerciseResolutionResult;
import com.shapyfy.core.domain.model.TranslationKeys;
import com.shapyfy.core.domain.port.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class Exercises {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseDeduplicationService deduplicationService;
    private final TranslationService translationService;

    /**
     * Create exercise with deduplication and translation support.
     * Returns resolution result indicating success or conflict.
     */
    public ExerciseResolutionResult createWithDeduplication(String name, String userLanguage) {
        log.info("Attempt to create exercise '{}' for language '{}'", name, userLanguage);

        // Check for duplicates
        ExerciseResolutionResult resolution = deduplicationService.resolveExercise(name);

        if (resolution.isConflict()) {
            log.info("Exercise creation conflict: exercise already exists");
            return resolution;
        }

        // Create exercise with canonical translation key
        String translationKey = resolution.canonicalTranslationKey();
        Exercise exercise = exerciseRepository.save(Exercise.from(translationKey));
        log.info("Exercise created with translation key: {}", translationKey);

        // Save translations
        var detection = resolution.languageDetection();
        String category = TranslationKeys.extractCategory(translationKey);

        // Save English translation
        translationService.saveTranslation(category, translationKey, "en", detection.englishName());

        // Save original language translation if not English
        if (!detection.detectedLanguage().equals("en")) {
            translationService.saveTranslation(category, translationKey, detection.detectedLanguage(), detection.originalName());
        }

        return ExerciseResolutionResult.success(exercise, detection);
    }

    /**
     * Legacy create method for backward compatibility.
     * @deprecated Use createWithDeduplication instead
     */
    @Deprecated
    public Exercise create(String name) {
        log.info("Attempt to create exercise {} (legacy method)", name);
        String translationKey = TranslationKeys.forExercise(name);
        Exercise exercise = exerciseRepository.save(Exercise.from(translationKey));
        log.info("Exercise created {}", exercise);
        return exercise;
    }

    public Exercise fetchById(Exercise.ExerciseId exerciseId) {
        return exerciseRepository.getById(exerciseId);
    }

    public List<Exercise> fetchAll() {
        return exerciseRepository.findAll();
    }
}
