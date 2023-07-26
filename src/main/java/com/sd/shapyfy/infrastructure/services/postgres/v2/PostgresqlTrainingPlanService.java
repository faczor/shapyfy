package com.sd.shapyfy.infrastructure.services.postgres.v2;

import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.domain.plan.PlanConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanCreationInitialConfigurationParams;
import com.sd.shapyfy.domain.plan.TrainingPlanService;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.PostgresTrainingRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.v2.converter.PlanConfigurationToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.v2.converter.TrainingPlanToEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresqlTrainingPlanService implements TrainingPlanService {

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingPlanToEntityConverter trainingPlanToEntityConverter;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    private final PostgresqlTrainingDayService trainingDayService;

    @Override
    public PlanConfiguration create(PlanCreationInitialConfigurationParams configurationParams, UserId userId) {
        log.info("Attempt to create training plan for user {} with configuration {}", userId, configurationParams);
        TrainingEntity savedTraining = trainingRepository.save(trainingPlanToEntityConverter.onCreation(configurationParams.name(), userId));
        List<TrainingDayEntity> savedTrainingDays = configurationParams.sessionDayConfigurations().stream()
                .map(day -> trainingDayService.initializeTrainingDay(day, savedTraining)).toList();

        return planConfigurationToDomainConverter.convert(savedTraining, savedTrainingDays);
    }
}
