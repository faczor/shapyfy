package com.shapyfy.core.architecture.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExerciseCrudRepository : CrudRepository<ExerciseEntity, UUID> {
    fun findByName(name: String): ExerciseEntity?
    fun findAllBy(): List<ExerciseEntity>
}

@Repository
interface TranslationCrudRepository : CrudRepository<TranslationEntity, Long> {
    fun findByNormalizedValue(normalizedValue: String): TranslationEntity?
    fun findAllByTranslationKey(translationKey: String): List<TranslationEntity>
    fun findAllByLanguage(language: String): List<TranslationEntity>
}
