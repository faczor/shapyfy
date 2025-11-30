package com.shapyfy.core.architecture.translation

import com.shapyfy.core.domain.exercise.Exercise
import com.shapyfy.core.domain.ExerciseId
import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.exercise.*
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
            canonicalName = CanonicalExerciseName("bench_press"),
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = setOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            equipmentRequired = setOf(Equipment.BARBELL, Equipment.BENCH),
            difficulty = Difficulty.BEGINNER,
            movementPattern = MovementPattern.PUSH,
            createdAt = Instant.now(),
            updatedAt = null
        )

        translator.storeTranslations(
            exercise = exercise,
            rawName = "Bankdrücken",
            preferredLanguage = Language.PL,
            detectedLanguage = Language.EN
        )

        // Should store translations for ALL languages (EN + PL)
        // But should NOT use user's raw input "Bankdrücken" because detector disagrees
        assertThat(catalog.savedRecords).hasSize(2)

        val enRecord = catalog.savedRecords.find { it.language == Language.EN }
        assertThat(enRecord).isNotNull
        assertThat(enRecord!!.value).isEqualTo("Bench Press")

        val plRecord = catalog.savedRecords.find { it.language == Language.PL }
        assertThat(plRecord).isNotNull
        // Should be AI-translated, not user's raw input "Bankdrücken"
        assertThat(plRecord!!.value).isNotEqualTo("Bankdrücken")
        assertThat(plRecord.value).isEqualTo("Polski: Bench Press")  // From mock
    }

    private class RecordingTranslationCatalog : TranslationCatalogPort {
        var savedRecords: List<TranslationRecord> = emptyList()

        override fun findByNormalizedValue(normalizedValue: String): TranslationRecord? =
            null

        override fun findAllByKey(translationKey: TranslationKey): List<TranslationRecord> =
            emptyList()

        override fun save(record: TranslationRecord) {
            savedRecords = listOf(record)
        }

        override fun saveAll(records: Collection<TranslationRecord>) {
            savedRecords = records.toList()
        }

        override fun fetchValues(
            language: Language,
            keys: Collection<TranslationKey>
        ): Map<TranslationKey, String> = emptyMap()
    }

    private object UnusedAiTranslationClient : AiTranslationClient {
        override fun detectAndTranslate(name: String): AiTranslationResult =
            throw UnsupportedOperationException("not required for this test")

        override fun translate(englishName: String, targetLanguage: Language): String {
            return when (targetLanguage) {
                Language.EN -> englishName
                Language.PL -> "Polski: $englishName"  // Simple placeholder
            }
        }
    }
}
