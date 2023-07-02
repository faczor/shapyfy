package com.sd.shapyfy.infrastructure.postgres.trainings;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingPort;
import com.sd.shapyfy.infrastructure.postgres.TrainingDomainEntityBiDirectionalConverter;
import com.sd.shapyfy.infrastructure.postgres.trainingDay.PostgresTrainingDayPort;
import com.sd.shapyfy.infrastructure.postgres.trainingDay.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingPort implements TrainingPort {

    private final TrainingDomainEntityBiDirectionalConverter trainingDomainEntityConverter;

    private final PostgresTrainingDayPort trainingDayPort;

    private final PostgresTrainingRepository trainingRepository;

    @Override
    public Training save(Training training) {
        log.info("Saving training {} to postgres repository", training);
        TrainingEntity savedEntity = trainingRepository.save(trainingDomainEntityConverter.toEntity(training));
        ArrayList<TrainingDayEntity> sessionsCreated = trainingDayPort.createSessionsFor(savedEntity, training.getTrainingDays());
        savedEntity.setDays(sessionsCreated);
        return trainingDomainEntityConverter.toDomain(savedEntity);
    }
}
