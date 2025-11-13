package com.shapyfy.core.infrastructure.ai;

import com.shapyfy.core.domain.model.LanguageDetection;
import com.shapyfy.core.domain.port.ExerciseAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Simple implementation of ExerciseAiService with basic heuristics.
 * TODO: Replace with actual AI integration (OpenAI, Claude, etc.)
 */
@Slf4j
@Service
public class SimpleExerciseAiService implements ExerciseAiService {

    private static final String POLISH_CHARS_REGEX = ".*[ąćęłńóśźż].*";

    @Override
    public LanguageDetection detectAndTranslate(String exerciseName) {
        log.debug("Detecting language for exercise: {}", exerciseName);

        // Simple heuristic: check for Polish-specific characters
        if (exerciseName.toLowerCase().matches(POLISH_CHARS_REGEX)) {
            log.debug("Detected Polish characters in: {}", exerciseName);
            // TODO: Replace with actual AI translation
            return LanguageDetection.of(
                    exerciseName,
                    "pl",
                    exerciseName, // For now, return original name - AI will translate
                    0.8f
            );
        }

        // Common Polish exercise names (hardcoded for now)
        String lowerName = exerciseName.toLowerCase();
        if (lowerName.contains("wyciskanie")) {
            return LanguageDetection.of(exerciseName, "pl", "Bench Press", 0.9f);
        } else if (lowerName.contains("przysiady") || lowerName.contains("przysiad")) {
            return LanguageDetection.of(exerciseName, "pl", "Squat", 0.9f);
        } else if (lowerName.contains("martwy ciąg")) {
            return LanguageDetection.of(exerciseName, "pl", "Deadlift", 0.9f);
        }

        // Assume English
        log.debug("Assuming English for: {}", exerciseName);
        return LanguageDetection.assumeEnglish(exerciseName);
    }
}
