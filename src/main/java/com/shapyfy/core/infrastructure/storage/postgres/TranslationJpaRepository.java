package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationJpaRepository extends JpaRepository<Translation, Long> {

    List<Translation> findByCategoryAndLanguage(String category, String language);

    Optional<Translation> findByTranslationKeyAndLanguage(String translationKey, String language);

    List<Translation> findByCategory(String category);
}
