package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingPlanJpaRepository extends JpaRepository<TrainingPlan, TrainingPlan.TrainingPlanId> {
    Optional<TrainingPlan> findByUserId(UserId userId);
}
