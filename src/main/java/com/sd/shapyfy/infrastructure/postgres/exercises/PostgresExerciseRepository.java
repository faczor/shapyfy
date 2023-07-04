package com.sd.shapyfy.infrastructure.postgres.exercises;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostgresExerciseRepository extends JpaRepository<ExerciseEntity, UUID> {
}
