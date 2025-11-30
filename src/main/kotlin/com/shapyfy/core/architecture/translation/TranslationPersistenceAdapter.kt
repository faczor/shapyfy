package com.shapyfy.core.architecture.translation

import com.shapyfy.core.architecture.persistence.TranslationCrudRepository
import com.shapyfy.core.architecture.persistence.TranslationEntity
import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.exercise.TranslationKey
import com.shapyfy.core.domain.exercise.TranslationRecord
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Component

@Component
class TranslationPersistenceAdapter(
    private val translationCrudRepository: TranslationCrudRepository
) : TranslationCatalogPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun findByNormalizedValue(normalizedValue: String): TranslationRecord? =
        withDataAccessLogging("findByNormalizedValue normalizedValue=$normalizedValue") {
            translationCrudRepository
                .findByNormalizedValue(normalizedValue)
                ?.toDomain()
        }

    override fun findAllByKey(translationKey: TranslationKey): List<TranslationRecord> =
        withDataAccessLogging("findAllByKey key=${translationKey.value}") {
            translationCrudRepository.findAllByTranslationKey(translationKey.value)
                .map { it.toDomain() }
        }

    override fun save(record: TranslationRecord) {
        log.info(
            "Attempt to save translation for key {} language {}",
            record.translationKey.value,
            record.language.code
        )
        withDataAccessLogging("save key=${record.translationKey.value}") {
            translationCrudRepository.save(record.toEntity())
        }
    }

    override fun saveAll(records: Collection<TranslationRecord>) {
        if (records.isEmpty()) {
            return
        }
        log.info(
            "Attempt to save {} translations for keys {}",
            records.size,
            records.map { it.translationKey.value }.distinct()
        )

        withDataAccessLogging("saveAll batchSize=${records.size}") {
            translationCrudRepository.saveAll(records.map { it.toEntity() })
        }
    }

    override fun fetchValues(
        language: Language,
        keys: Collection<TranslationKey>
    ): Map<TranslationKey, String> {
        if (keys.isEmpty()) {
            return emptyMap()
        }
        log.info(
            "Attempt to fetch {} translations for language {}",
            keys.size,
            language.code
        )

        val translationMap = withDataAccessLogging("fetchValues language=${language.code}") {
            translationCrudRepository
                .findAllByLanguage(language.code)
                .associate { it.translationKey to it.value }
        }

        return keys.associateWith { translationMap[it.value] ?: it.value }
    }

    private fun TranslationRecord.toEntity() =
        TranslationEntity(
            id = null,
            translationKey = translationKey.value,
            language = language.code,
            value = value,
            normalizedValue = normalizedValue,
            createdAt = java.time.Instant.now(),
            updatedAt = java.time.Instant.now()
        )

    private fun TranslationEntity.toDomain(): TranslationRecord =
        TranslationRecord(
            translationKey = TranslationKey(translationKey),
            language = Language.from(language),
            value = value,
            normalizedValue = normalizedValue
        )

    private fun <T> withDataAccessLogging(operation: String, block: () -> T): T =
        try {
            block()
        } catch (ex: DataAccessException) {
            log.error("Translation persistence operation '{}' failed", operation, ex)
            throw ex
        }
}
