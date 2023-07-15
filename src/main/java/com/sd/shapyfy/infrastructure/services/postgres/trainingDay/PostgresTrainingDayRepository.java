package com.sd.shapyfy.infrastructure.services.postgres.trainingDay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostgresTrainingDayRepository extends JpaRepository<TrainingDayEntity, UUID> {
}
