package com.sd.shapyfy.infrastructure.services.postgres.v2.converter;

import com.sd.shapyfy.domain.plan.PlanConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanConfigurationToDomainConverter {

    private final TrainingPlanToDomainConverter trainingPlanToDomainConverter;

    private final TrainingDayToDomainConverter trainingDayToDomainConverter;

    public PlanConfiguration convert(TrainingEntity trainingEntity, List<TrainingDayEntity> trainingDayEntities) {

        return new PlanConfiguration(
                trainingPlanToDomainConverter.convert(trainingEntity),
                trainingDayEntities.stream().map(trainingDayToDomainConverter::toConfiguration).toList()
        );
    }
}
