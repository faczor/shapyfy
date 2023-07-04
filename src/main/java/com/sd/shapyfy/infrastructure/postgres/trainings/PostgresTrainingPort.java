package com.sd.shapyfy.infrastructure.postgres.trainings;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingPort;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.infrastructure.postgres.trainings.converter.TrainingToEntityConverter;
import com.sd.shapyfy.infrastructure.postgres.trainingDay.PostgresTrainingDayPort;
import com.sd.shapyfy.infrastructure.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.postgres.trainings.converter.TrainingEntityToDomainConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingPort implements TrainingPort {

    private final TrainingToEntityConverter trainingDomainEntityConverter;

    private final PostgresTrainingDayPort trainingDayPort;

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    @Override
    public Training save(Training training) {
        log.info("Saving training {} to postgres repository", training);
        TrainingEntity savedEntity = trainingRepository.save(trainingDomainEntityConverter.convert(training));
        ArrayList<TrainingDayEntity> sessionsCreated = trainingDayPort.createSessionsFor(savedEntity, training.getTrainingDays());
        savedEntity.setDays(sessionsCreated);
        return trainingEntityToDomainConverter.convert(savedEntity);
    }

    @Override
    public Training fetchFor(TrainingDayId trainingDayId) {
        log.info("Attempt to fetch training for day with {}", trainingDayId);
        TrainingEntity trainingEntity = trainingRepository.findByDaysId(trainingDayId.getValue())
                .orElseThrow(() -> new TrainingNotFound("Training not found for " + trainingDayId));

        return trainingEntityToDomainConverter.convert(trainingEntity);
    }
}
