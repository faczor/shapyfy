package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.*;
import com.shapyfy.core.domain.port.ExerciseAiService;
import com.shapyfy.core.domain.port.ExerciseRepository;
import com.shapyfy.core.domain.port.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for detecting duplicate exercises across languages
 * and resolving conflicts using AI-powered translation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseDeduplicationService {

    private final ExerciseRepository exerciseRepository;
    private final TranslationRepository translationRepository;
    private final ExerciseAiService aiService;

    /**
     * Resolve exercise name to check for duplicates.
     * Uses AI to detect language and translate to canonical English form.
     *
     * @param name The exercise name in any language
     * @return Resolution result indicating conflict or allowing creation
     */
    public ExerciseResolutionResult resolveExercise(String name) {
        log.info("Resolving exercise name: {}", name);

        // Step 1: Quick lookup - check if exact translation exists
        String normalizedKey = TranslationKeys.forExercise(name);
        Optional<Translation> existingTranslation = findTranslationInAnyLanguage(normalizedKey);

        if (existingTranslation.isPresent()) {
            log.info("Found existing translation for normalized key: {}", normalizedKey);
            return handleExistingTranslation(existingTranslation.get(), name);
        }

        // Step 2: AI-powered detection and translation
        LanguageDetection detection = aiService.detectAndTranslate(name);
        log.info("AI detected language: {}, English name: {}, confidence: {}",
                detection.detectedLanguage(), detection.englishName(), detection.confidence());

        // Step 3: Check if canonical English exercise exists
        String canonicalKey = TranslationKeys.forExercise(detection.englishName());
        Optional<Translation> canonicalTranslation = findTranslationInAnyLanguage(canonicalKey);

        if (canonicalTranslation.isPresent()) {
            log.info("Found existing exercise with canonical English name: {}", detection.englishName());
            return handleExistingTranslation(canonicalTranslation.get(), name);
        }

        // Step 4: No conflict - allow creation
        log.info("No conflict found, allowing creation of new exercise: {}", name);
        return ExerciseResolutionResult.allowCreation(detection, canonicalKey);
    }

    /**
     * Find translation by key in any language.
     * Used to detect if exercise already exists regardless of language.
     */
    private Optional<Translation> findTranslationInAnyLanguage(String translationKey) {
        // Check both English and Polish (can be extended)
        return translationRepository.findByTranslationKeyAndLanguage(translationKey, "en")
                .or(() -> translationRepository.findByTranslationKeyAndLanguage(translationKey, "pl"));
    }

    /**
     * Handle case where translation already exists.
     * Find the corresponding exercise and return conflict.
     */
    private ExerciseResolutionResult handleExistingTranslation(Translation existingTranslation, String originalName) {
        String translationKey = existingTranslation.getTranslationKey();

        // Find exercise with this translation key
        Optional<Exercise> existingExercise = exerciseRepository.findByTranslationKey(translationKey);

        if (existingExercise.isEmpty()) {
            log.warn("Found translation {} but no corresponding exercise - data inconsistency!", translationKey);
            // This shouldn't happen, but if it does, allow creation
            LanguageDetection detection = LanguageDetection.assumeEnglish(originalName);
            return ExerciseResolutionResult.allowCreation(detection, translationKey);
        }

        LanguageDetection detection = LanguageDetection.of(
                originalName,
                existingTranslation.getLanguage(),
                existingTranslation.getValue(),
                0.95f
        );

        log.info("Found existing exercise: {} (translation key: {})", existingExercise.get().getId(), translationKey);
        return ExerciseResolutionResult.conflict(existingExercise.get(), detection, existingTranslation.getValue());
    }
}
