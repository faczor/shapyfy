package com.sd.shapyfy.infrastructure.services.postgres.exercises.component;

import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostgresExerciseRepository extends JpaRepository<ExerciseEntity, UUID> {
}
