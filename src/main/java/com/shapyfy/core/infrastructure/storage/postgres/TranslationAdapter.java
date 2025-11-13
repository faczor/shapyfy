package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.Translation;
import com.shapyfy.core.domain.port.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranslationAdapter implements TranslationRepository {

    private final TranslationJpaRepository translationJpaRepository;

    @Override
    public Translation save(Translation translation) {
        log.debug("Saving translation: key={}, language={}", translation.getTranslationKey(), translation.getLanguage());
        return translationJpaRepository.save(translation);
    }

    @Override
    public List<Translation> findByCategoryAndLanguage(String category, String language) {
        log.debug("Finding translations by category={}, language={}", category, language);
        return translationJpaRepository.findByCategoryAndLanguage(category, language);
    }

    @Override
    public Optional<Translation> findByTranslationKeyAndLanguage(String translationKey, String language) {
        log.debug("Finding translation by key={}, language={}", translationKey, language);
        return translationJpaRepository.findByTranslationKeyAndLanguage(translationKey, language);
    }

    @Override
    public List<Translation> findByCategory(String category) {
        log.debug("Finding translations by category={}", category);
        return translationJpaRepository.findByCategory(category);
    }
}
