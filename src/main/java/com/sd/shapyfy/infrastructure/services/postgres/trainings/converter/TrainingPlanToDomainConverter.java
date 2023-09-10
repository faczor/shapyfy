package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.boundary.api.plans.contract.PlanState;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.domain.plan.model.Plan;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingPlanToDomainConverter {

    public Plan convert(TrainingEntity trainingEntity) {
        return new Plan(
                PlanId.of(trainingEntity.getId()),
                trainingEntity.getName(),
                UserId.of(trainingEntity.getUserId()),
                PlanState.NOT_STARTED
        );
    }
}
