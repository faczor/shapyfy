package com.sd.shapyfy.infrastructure.services.postgres.trainings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostgresTrainingRepository extends JpaRepository<TrainingEntity, UUID> {

    @Query(value = "SELECT t FROM TrainingEntity AS t JOIN t.days td WHERE td.id = :trainingDayId")
    Optional<TrainingEntity> findByDaysId(UUID trainingDayId);

}
