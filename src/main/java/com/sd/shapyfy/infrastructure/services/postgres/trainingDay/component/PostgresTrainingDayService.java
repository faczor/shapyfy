package com.sd.shapyfy.infrastructure.services.postgres.trainingDay.component;

import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanCreationInitialConfigurationParams.SessionDayConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayToEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresTrainingDayService {

    private final PostgresTrainingDayRepository trainingDayRepository;


    private final TrainingDayToEntityConverter trainingDayToEntityConverter;


    public TrainingDayEntity initializeTrainingDay(SessionDayConfiguration sessionDayConfiguration, TrainingEntity training) {
        log.info("Attempt to initialize training day with configuration {}", sessionDayConfiguration);

        TrainingDayEntity savedDay = trainingDayRepository.save(trainingDayToEntityConverter.onInitialization(sessionDayConfiguration, training));
        log.info("Training day initialized with configuration {}", savedDay);
        return savedDay;
    }
}
