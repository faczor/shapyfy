package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.domain.port.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingPlanAdapter implements TrainingPlanRepository {

    private final TrainingPlanJpaRepository trainingPlanJpaRepository;

    @Override
    public TrainingPlan save(TrainingPlan trainingPlan) {
        return trainingPlanJpaRepository.save(trainingPlan);
    }

    @Override
    public TrainingPlan findByUserId(UserId userId) {
        return trainingPlanJpaRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Training plan not found for user " + userId.getId()));
    }
}
