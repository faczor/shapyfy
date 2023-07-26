package com.sd.shapyfy.infrastructure.services.postgres.v2.converter;

import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.domain.plan.Plan;
import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingPlanToDomainConverter {

    public Plan convert(TrainingEntity trainingEntity) {
        return new Plan(
                PlanId.of(trainingEntity.getId()),
                trainingEntity.getName(),
                UserId.of(trainingEntity.getUserId())
        );
    }
}
