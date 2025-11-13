package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.Translation;
import com.shapyfy.core.domain.model.TranslationKeys;
import com.shapyfy.core.domain.port.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;

    /**
     * Load all translations for a category and language from database.
     * Results are cached to minimize database queries.
     *
     * @param category The translation category (e.g., "exercises", "ui")
     * @param language The language code (e.g., "en", "pl")
     * @return Map of translation keys to their values
     */
    @Cacheable(value = "translations", key = "#category + '_' + #language")
    public Map<String, String> loadTranslations(String category, String language) {
        log.info("Loading translations from DB for category={}, language={}", category, language);

        List<Translation> translations = translationRepository.findByCategoryAndLanguage(category, language);

        log.debug("Loaded {} translations for category={}, language={}", translations.size(), category, language);

        return translations.stream()
                .collect(Collectors.toMap(
                        Translation::getTranslationKey,
                        Translation::getValue
                ));
    }

    /**
     * Translate a single key to the specified language.
     * Uses cached translations for performance.
     *
     * @param key      The translation key (e.g., "exercises.bench_press")
     * @param language The language code (e.g., "en", "pl")
     * @return The translated value, or the key itself if translation not found
     */
    public String translate(String key, String language) {
        String category = TranslationKeys.extractCategory(key);
        Map<String, String> translations = loadTranslations(category, language);

        String value = translations.get(key);
        if (value == null) {
            log.warn("Translation not found for key={}, language={}, returning key as fallback", key, language);
            return key;
        }

        return value;
    }

    /**
     * Translate multiple keys in batch for performance.
     * More efficient than calling translate() multiple times.
     *
     * @param keys     List of translation keys to translate
     * @param language The language code
     * @return Map of translation keys to their translated values
     */
    public Map<String, String> translateBatch(List<String> keys, String language) {
        if (keys == null || keys.isEmpty()) {
            return Map.of();
        }

        // Assume all keys are from the same category (should be validated in caller)
        String category = TranslationKeys.extractCategory(keys.get(0));
        Map<String, String> translations = loadTranslations(category, language);

        return keys.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> translations.getOrDefault(key, key) // Fallback to key if not found
                ));
    }

    /**
     * Save a translation to the database and evict the cache.
     * This ensures the cache is refreshed on next access.
     *
     * @param category       The translation category
     * @param translationKey The full translation key
     * @param language       The language code
     * @param value          The translated value
     */
    @CacheEvict(value = "translations", key = "#category + '_' + #language")
    public void saveTranslation(String category, String translationKey, String language, String value) {
        log.info("Saving translation: key={}, language={}, value={}", translationKey, language, value);

        Translation translation = Translation.create(translationKey, language, value, category);
        translationRepository.save(translation);

        log.debug("Translation saved and cache evicted for category={}, language={}", category, language);
    }

    /**
     * Save or update a translation.
     * If translation exists, update the value. Otherwise, create new.
     *
     * @param category       The translation category
     * @param translationKey The full translation key
     * @param language       The language code
     * @param value          The translated value
     */
    @CacheEvict(value = "translations", key = "#category + '_' + #language")
    public void saveOrUpdateTranslation(String category, String translationKey, String language, String value) {
        log.info("Saving or updating translation: key={}, language={}, value={}", translationKey, language, value);

        var existingOpt = translationRepository.findByTranslationKeyAndLanguage(translationKey, language);

        if (existingOpt.isPresent()) {
            Translation existing = existingOpt.get();
            Translation updated = Translation.of(
                    existing.getId(),
                    existing.getTranslationKey(),
                    existing.getLanguage(),
                    value, // Update value
                    existing.getCategory(),
                    existing.getCreatedAt(),
                    existing.getUpdatedAt()
            );
            translationRepository.save(updated);
            log.debug("Translation updated for key={}, language={}", translationKey, language);
        } else {
            Translation translation = Translation.create(translationKey, language, value, category);
            translationRepository.save(translation);
            log.debug("Translation created for key={}, language={}", translationKey, language);
        }
    }
}
