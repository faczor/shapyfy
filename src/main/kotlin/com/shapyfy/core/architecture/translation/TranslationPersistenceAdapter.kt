package com.shapyfy.core.architecture.translation

import com.shapyfy.core.architecture.persistence.TranslationCrudRepository
import com.shapyfy.core.architecture.persistence.TranslationEntity
import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.exercise.TranslationCategory
import com.shapyfy.core.domain.exercise.TranslationKey
import com.shapyfy.core.domain.exercise.TranslationRecord
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Component

private const val TRANSLATION_CACHE = "translations"

@Component
class TranslationPersistenceAdapter(
    private val translationCrudRepository: TranslationCrudRepository,
    private val cacheManager: CacheManager
) : TranslationCatalogPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun findByNormalizedValue(
        category: TranslationCategory,
        normalizedValue: String
    ): TranslationRecord? =
        withDataAccessLogging("findByNormalizedValue category=${category.value} normalizedValue=$normalizedValue") {
            translationCrudRepository
                .findByCategoryAndNormalizedValue(category.value, normalizedValue)
                ?.toDomain()
        }

    override fun findAllByKey(
        category: TranslationCategory,
        translationKey: TranslationKey
    ): List<TranslationRecord> =
        withDataAccessLogging("findAllByKey category=${category.value} key=${translationKey.value}") {
            translationCrudRepository.findAllByTranslationKey(translationKey.value)
                .filter { it.category == category.value }
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
        evictCache(record.category, record.language)
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
        records.forEach { evictCache(it.category, it.language) }
    }

    override fun fetchValues(
        category: TranslationCategory,
        language: Language,
        keys: Collection<TranslationKey>
    ): Map<TranslationKey, String> {
        if (keys.isEmpty()) {
            return emptyMap()
        }
        log.info(
            "Attempt to fetch {} translations for category {} and language {}",
            keys.size,
            category.value,
            language.code
        )

        val categoryMap = getOrLoadCategory(language, category)
        return keys.associateWith { categoryMap[it.value] ?: it.value }
    }

    private fun getOrLoadCategory(language: Language, category: TranslationCategory): Map<String, String> {
        val cacheKey = cacheKey(category, language)
        val cache = cacheManager.getCache(TRANSLATION_CACHE)
        val cached = cache?.get(cacheKey, Map::class.java) as? Map<String, String>
        if (cached != null) {
            return cached
        }

        log.info(
            "Cache miss for translations category={} language={}, loading from DB",
            category.value,
            language.code
        )

        val loaded = withDataAccessLogging("loadCategory category=${category.value} language=${language.code}") {
            translationCrudRepository
                .findAllByCategoryAndLanguage(category.value, language.code)
                .associate { it.translationKey to it.value }
        }

        cache?.put(cacheKey, loaded)
        return loaded
    }

    private fun evictCache(category: TranslationCategory, language: Language) {
        cacheManager.getCache(TRANSLATION_CACHE)?.evict(cacheKey(category, language))
    }

    private fun cacheKey(category: TranslationCategory, language: Language): String =
        "${category.value}_${language.code}"

    private fun TranslationRecord.toEntity() =
        TranslationEntity(
            id = null,
            translationKey = translationKey.value,
            language = language.code,
            value = value,
            category = category.value,
            normalizedValue = normalizedValue,
            createdAt = java.time.Instant.now(),
            updatedAt = java.time.Instant.now()
        )

    private fun TranslationEntity.toDomain(): TranslationRecord =
        TranslationRecord(
            translationKey = TranslationKey(translationKey),
            language = Language.from(language),
            value = value,
            normalizedValue = normalizedValue,
            category = TranslationCategory.fromValue(category)
        )

    private fun <T> withDataAccessLogging(operation: String, block: () -> T): T =
        try {
            block()
        } catch (ex: DataAccessException) {
            log.error("Translation persistence operation '{}' failed", operation, ex)
            throw ex
        }
}
