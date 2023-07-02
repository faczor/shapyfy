package com.sd.shapyfy.infrastructure.postgres.trainings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostgresTrainingRepository extends JpaRepository<TrainingEntity, UUID> {
}
