package com.sd.shapyfy.infrastructure.services.postgres.trainings.component;

import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostgresTrainingRepository extends JpaRepository<TrainingEntity, UUID> {

    List<TrainingEntity> findAllByUserId(String userId);
}
