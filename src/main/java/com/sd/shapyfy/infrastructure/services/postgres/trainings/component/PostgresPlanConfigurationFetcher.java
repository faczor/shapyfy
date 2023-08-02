package com.sd.shapyfy.infrastructure.services.postgres.trainings.component;

import com.sd.shapyfy.domain.configuration.PlanConfigurationFetcher;
import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingNotFound;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.PlanConfigurationToDomainConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresPlanConfigurationFetcher implements PlanConfigurationFetcher {

    private final PostgresTrainingRepository trainingRepository;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    @Override
    public PlanConfiguration trainingConfigurationBy(PlanId planId) {
        TrainingEntity training = findById(planId);

        return planConfigurationToDomainConverter.convert(training, training.getDays());
    }

    private TrainingEntity findById(PlanId planId) {
        return trainingRepository.findById(planId.getValue()).orElseThrow(() -> new TrainingNotFound("Not found by" + planId));
    }
}
