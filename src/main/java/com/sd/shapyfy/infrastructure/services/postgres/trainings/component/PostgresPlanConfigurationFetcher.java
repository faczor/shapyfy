package com.sd.shapyfy.infrastructure.services.postgres.trainings.component;

import com.sd.shapyfy.domain.configuration.PlanConfigurationFetcher;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingNotFound;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.PlanConfigurationToDomainConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresPlanConfigurationFetcher implements PlanConfigurationFetcher {

    private final PostgresTrainingRepository trainingRepository;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    private final TrainingToDomainConverter trainingToDomainConverter;

    @Override
    public TrainingConfiguration trainingConfigurationBy(PlanId planId) {
        TrainingEntity training = findById(planId);

        return planConfigurationToDomainConverter.convert(training);
    }

    @Override
    public List<com.sd.shapyfy.domain.plan.model.Training> fetchAllTrainingsFor(UserId userId) {
        List<TrainingEntity> allByUserId = trainingRepository.findAllByUserId(userId.getValue());
        return allByUserId.stream().map(trainingToDomainConverter::convert).toList();
    }

    private TrainingEntity findById(PlanId planId) {
        return trainingRepository.findById(planId.getValue()).orElseThrow(() -> new TrainingNotFound("Not found by" + planId));
    }
}
