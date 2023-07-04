package com.sd.shapyfy.infrastructure.postgres.trainingDayExercises;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresTrainingDayExerciseRepository extends JpaRepository<TrainingDayExerciseEntity, UUID> {
}
