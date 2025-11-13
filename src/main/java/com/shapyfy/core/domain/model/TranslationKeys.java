package com.shapyfy.core.domain.model;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;
import java.util.Locale;

@UtilityClass
public class TranslationKeys {

    private static final String EXERCISES_CATEGORY = "exercises";
    private static final String UI_CATEGORY = "ui";
    private static final String ERRORS_CATEGORY = "errors";

    /**
     * Generate translation key for exercise name
     * Example: "Bench Press" → "exercises.bench_press"
     *          "Wyciskanie na ławce" → "exercises.wyciskanie_na_lawce"
     */
    public static String forExercise(String exerciseName) {
        return EXERCISES_CATEGORY + "." + normalize(exerciseName);
    }

    /**
     * Generate translation key for UI labels
     * Example: "Save Button" → "ui.save_button"
     */
    public static String forUi(String label) {
        return UI_CATEGORY + "." + normalize(label);
    }

    /**
     * Generate translation key for error messages
     * Example: "Not Found" → "errors.not_found"
     */
    public static String forError(String errorName) {
        return ERRORS_CATEGORY + "." + normalize(errorName);
    }

    /**
     * Extract category from translation key
     * Example: "exercises.bench_press" → "exercises"
     */
    public static String extractCategory(String translationKey) {
        if (translationKey == null || !translationKey.contains(".")) {
            return translationKey;
        }
        return translationKey.substring(0, translationKey.indexOf("."));
    }

    /**
     * Normalize string to create consistent translation key
     * - Removes diacritics (ą → a, ł → l)
     * - Converts to lowercase
     * - Replaces non-alphanumeric with underscore
     * - Removes duplicate underscores
     * - Trims underscores from start/end
     */
    private static String normalize(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Translation key input cannot be null or blank");
        }

        // Remove diacritics (ą, ć, ę, ł, ń, ó, ś, ź, ż → a, c, e, l, n, o, s, z, z)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Convert to lowercase and replace non-alphanumeric with underscore
        normalized = normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_");

        // Remove duplicate underscores
        normalized = normalized.replaceAll("_+", "_");

        // Trim underscores from start/end
        normalized = normalized.replaceAll("^_|_$", "");

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Normalized translation key cannot be blank for input: " + input);
        }

        return normalized;
    }
}
