package com.sd.shapyfy.infrastructure.services.postgres.trainings.component;

import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanService;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationEntity;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models.RequestToCreationParamsConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.CreationParamsConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresTrainingPlanService implements TrainingPlanService {

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    private final RequestToCreationParamsConverter requestToCreationParamsConverter;

    @Override
    public TrainingConfiguration create(PlanConfiguration configurationParams, UserId userId) {
        log.info("Attempt to create training plan for user {} with configuration {}", userId, configurationParams);
        TrainingEntity training = TrainingEntity.create(configurationParams.name(), userId);
        training.setConfiguration(ConfigurationEntity.create(requestToCreationParamsConverter.convert(configurationParams)));

        save(training);
        return trainingEntityToDomainConverter.convertToConfiguration(training);
    }

    //TODO proper exception
    public TrainingEntity findById(PlanId planId) {
        return trainingRepository.findById(planId.getValue())
                .orElseThrow();
    }

    public TrainingEntity save(TrainingEntity training) {
        return trainingRepository.save(training);
    }

}
