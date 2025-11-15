package com.shapyfy.core.architecture.translation

import com.shapyfy.core.domain.exercise.Exercise
import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.exercise.TranslationCategory
import com.shapyfy.core.domain.exercise.TranslationKey
import com.shapyfy.core.domain.exercise.TranslationRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class ExerciseNameTranslatorTest {

    private val catalog = RecordingTranslationCatalog()
    private val translator = ExerciseNameTranslator(catalog, UnusedAiTranslationClient)

    @Test
    fun `should not attribute translation to preferred language when detector disagrees`() {
        val exercise = Exercise(
            id = ExerciseId.generate(),
            name = "bench_press",
            createdAt = Instant.now(),
            updatedAt = null
        )

        translator.storeTranslations(
            exercise = exercise,
            rawName = "Bankdrücken",
            preferredLanguage = Language.PL,
            detectedLanguage = Language.EN
        )

        assertThat(catalog.savedRecords).hasSize(1)
        val record = catalog.savedRecords.first()
        assertThat(record.language).isEqualTo(Language.EN)
        assertThat(record.value).isEqualTo("Bench Press")
    }

    private class RecordingTranslationCatalog : TranslationCatalogPort {
        var savedRecords: List<TranslationRecord> = emptyList()

        override fun findByNormalizedValue(category: TranslationCategory, normalizedValue: String): TranslationRecord? =
            null

        override fun findAllByKey(category: TranslationCategory, translationKey: TranslationKey): List<TranslationRecord> =
            emptyList()

        override fun save(record: TranslationRecord) {
            savedRecords = listOf(record)
        }

        override fun saveAll(records: Collection<TranslationRecord>) {
            savedRecords = records.toList()
        }

        override fun fetchValues(
            category: TranslationCategory,
            language: Language,
            keys: Collection<TranslationKey>
        ): Map<TranslationKey, String> = emptyMap()
    }

    private object UnusedAiTranslationClient : AiTranslationClient {
        override fun detectAndTranslate(name: String): AiTranslationResult =
            throw UnsupportedOperationException("not required for this test")
    }
}
