package com.shapyfy.core.architecture.classification

import com.shapyfy.core.domain.exercise.Difficulty
import com.shapyfy.core.domain.exercise.Equipment
import com.shapyfy.core.domain.exercise.MovementPattern
import com.shapyfy.core.domain.exercise.MuscleGroup
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExerciseClassificationAdapterTest {

    @Test
    fun `should map classification from client to domain properties`() {
        // Given
        val stubClient = StubClassificationClient(
            ExerciseClassification(
                primaryMuscleGroup = MuscleGroup.CHEST,
                secondaryMuscleGroups = setOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                equipmentRequired = setOf(Equipment.BARBELL, Equipment.BENCH),
                difficulty = Difficulty.BEGINNER,
                movementPattern = MovementPattern.PUSH
            )
        )
        val adapter = ExerciseClassificationAdapter(stubClient)

        // When
        val result = adapter.classify("Bench Press")

        // Then
        assertThat(result.primaryMuscleGroup).isEqualTo(MuscleGroup.CHEST)
        assertThat(result.secondaryMuscleGroups).containsExactlyInAnyOrder(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS)
        assertThat(result.equipmentRequired).containsExactlyInAnyOrder(Equipment.BARBELL, Equipment.BENCH)
        assertThat(result.difficulty).isEqualTo(Difficulty.BEGINNER)
        assertThat(result.movementPattern).isEqualTo(MovementPattern.PUSH)
    }

    @Test
    fun `should handle empty secondary muscle groups`() {
        // Given
        val stubClient = StubClassificationClient(
            ExerciseClassification(
                primaryMuscleGroup = MuscleGroup.BICEPS,
                secondaryMuscleGroups = emptySet(),
                equipmentRequired = setOf(Equipment.DUMBBELLS),
                difficulty = Difficulty.BEGINNER,
                movementPattern = MovementPattern.ISOLATION
            )
        )
        val adapter = ExerciseClassificationAdapter(stubClient)

        // When
        val result = adapter.classify("Bicep Curl")

        // Then
        assertThat(result.secondaryMuscleGroups).isEmpty()
        assertThat(result.primaryMuscleGroup).isEqualTo(MuscleGroup.BICEPS)
    }

    @Test
    fun `should handle bodyweight exercises`() {
        // Given
        val stubClient = StubClassificationClient(
            ExerciseClassification(
                primaryMuscleGroup = MuscleGroup.CHEST,
                secondaryMuscleGroups = setOf(MuscleGroup.TRICEPS, MuscleGroup.CORE),
                equipmentRequired = setOf(Equipment.BODYWEIGHT),
                difficulty = Difficulty.BEGINNER,
                movementPattern = MovementPattern.PUSH
            )
        )
        val adapter = ExerciseClassificationAdapter(stubClient)

        // When
        val result = adapter.classify("Push Up")

        // Then
        assertThat(result.equipmentRequired).containsOnly(Equipment.BODYWEIGHT)
    }

    private class StubClassificationClient(
        private val classification: ExerciseClassification
    ) : ExerciseClassificationClient {
        override fun classify(exerciseName: String): ExerciseClassification {
            return classification
        }
    }
}
