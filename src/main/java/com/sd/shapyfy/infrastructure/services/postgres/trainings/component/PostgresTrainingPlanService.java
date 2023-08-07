package com.sd.shapyfy.infrastructure.services.postgres.trainings.component;

import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanCreationInitialConfigurationParams;
import com.sd.shapyfy.domain.plan.TrainingPlanService;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.component.PostgresTrainingDayService;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.PlanConfigurationToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingPlanToEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresTrainingPlanService implements TrainingPlanService {

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingPlanToEntityConverter trainingPlanToEntityConverter;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    private final PostgresTrainingDayService trainingDayService;

    @Override
    public PlanConfiguration create(PlanCreationInitialConfigurationParams configurationParams, UserId userId) {
        log.info("Attempt to create training plan for user {} with configuration {}", userId, configurationParams);
        TrainingEntity savedTraining = trainingRepository.save(trainingPlanToEntityConverter.onCreation(configurationParams.name(), userId));
        List<TrainingDayEntity> savedTrainingDays = configurationParams.sessionDayConfigurations().stream()
                .map(day -> trainingDayService.initializeTrainingDay(day, savedTraining)).toList();

        return planConfigurationToDomainConverter.convert(savedTraining, savedTrainingDays);
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
