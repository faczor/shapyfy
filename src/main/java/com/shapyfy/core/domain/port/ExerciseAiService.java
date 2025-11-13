package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.model.LanguageDetection;

/**
 * Port for AI-powered exercise language detection and translation.
 * Implementations should integrate with OpenAI, Claude, or similar LLM services.
 */
public interface ExerciseAiService {

    /**
     * Detect language of exercise name and translate to canonical English.
     *
     * @param exerciseName The exercise name in any language
     * @return Language detection result with English translation
     */
    LanguageDetection detectAndTranslate(String exerciseName);
}
