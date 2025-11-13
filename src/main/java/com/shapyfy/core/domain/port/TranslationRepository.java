package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.model.Translation;

import java.util.List;
import java.util.Optional;

public interface TranslationRepository {

    Translation save(Translation translation);

    List<Translation> findByCategoryAndLanguage(String category, String language);

    Optional<Translation> findByTranslationKeyAndLanguage(String translationKey, String language);

    List<Translation> findByCategory(String category);
}
