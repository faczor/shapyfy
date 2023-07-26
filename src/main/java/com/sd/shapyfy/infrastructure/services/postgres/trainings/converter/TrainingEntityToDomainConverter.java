package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.model.Plan;
import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingEntityToDomainConverter {

    private final TrainingDayEntityToDomainConverter trainingDayEntityToDomainConverter;

    public Plan convert(TrainingEntity entity) {
        return new Plan(
                PlanId.of(entity.getId()),
                UserId.of(entity.getUserId()),
                entity.getName(),
                entity.getDays().stream().map(trainingDayEntityToDomainConverter::convert).toList()
        );
    }
}
