package com.shapyfy.core.domain.model;

/**
 * Result of AI language detection and translation.
 * Contains detected language, canonical English name, and confidence score.
 */
public record LanguageDetection(
        String originalName,
        String detectedLanguage,
        String englishName,
        float confidence
) {
    public static LanguageDetection of(String originalName, String detectedLanguage, String englishName, float confidence) {
        return new LanguageDetection(originalName, detectedLanguage, englishName, confidence);
    }

    public static LanguageDetection assumeEnglish(String name) {
        return new LanguageDetection(name, "en", name, 1.0f);
    }
}
