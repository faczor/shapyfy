package com.sd.shapyfy.infrastructure.services.postgres.v2;

import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanCreationInitialConfigurationParams.SessionDayConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresTrainingDayRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.v2.converter.TrainingDayToEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresqlTrainingDayService {

    private final PostgresTrainingDayRepository trainingDayRepository;

    private final TrainingDayToEntityConverter trainingDayToEntityConverter;


    public TrainingDayEntity initializeTrainingDay(SessionDayConfiguration sessionDayConfiguration, TrainingEntity training) {
        log.info("Attempt to initialize training day with configuration {}", sessionDayConfiguration);

        TrainingDayEntity savedDay = trainingDayRepository.save(trainingDayToEntityConverter.onInitialization(sessionDayConfiguration, training));
        log.info("Training day initialized with configuration {}", savedDay);
        return savedDay;
    }

}
