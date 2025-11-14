package com.shapyfy.core.architecture.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ExerciseCrudRepository : CrudRepository<ExerciseEntity, UUID> {
    fun findByName(name: String): ExerciseEntity?
    fun findAllBy(): List<ExerciseEntity>
}

@Repository
interface TranslationCrudRepository : CrudRepository<TranslationEntity, Long> {
    fun findByCategoryAndNormalizedValue(category: String, normalizedValue: String): TranslationEntity?
    fun findAllByTranslationKey(translationKey: String): List<TranslationEntity>
    fun findAllByCategoryAndLanguage(category: String, language: String): List<TranslationEntity>
    fun findAllByCategoryAndLanguageAndTranslationKeyIn(
        category: String,
        language: String,
        translationKeys: Collection<String>
    ): List<TranslationEntity>
}
