package com.sd.shapyfy.infrastructure.services.postgres.trainings;

import com.sd.shapyfy.domain.TrainingFetcher;
import com.sd.shapyfy.domain.model.Plan;
import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.domain.plan.PlanConfiguration;
import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.v2.PostgresqlTrainingPlanService;
import com.sd.shapyfy.infrastructure.services.postgres.v2.converter.PlanConfigurationToDomainConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresTrainingFetcher implements TrainingFetcher {

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    @Override
    public List<Plan> fetchFor(UserId userId) {
        Collection<TrainingEntity> allByUserId = trainingRepository.findAllByUserId(userId.getValue());

        return allByUserId.stream().map(trainingEntityToDomainConverter::convert).toList();
    }

    @Override
    public PlanConfiguration trainingConfigurationBy(PlanId planId) {
        TrainingEntity training = findById(planId);

        return planConfigurationToDomainConverter.convert(training, training.getDays());
    }

    private TrainingEntity findById(PlanId planId) {
        return trainingRepository.findById(planId.getValue()).orElseThrow(() -> new TrainingNotFound("Not found by" + planId));
    }
}
