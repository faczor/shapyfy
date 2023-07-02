package com.sd.shapyfy.infrastructure.postgres.trainingDay;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.infrastructure.postgres.TrainingDayDomainEntityBiDirectionalConverter;
import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingDayPort {

    private final PostgresTrainingDayRepository postgresTrainingDayRepository;

    private final TrainingDayDomainEntityBiDirectionalConverter trainingDayDomainEntityConverter;

    @Transactional
    public ArrayList<TrainingDayEntity> createSessionsFor(TrainingEntity parentTrainingEntity, List<Training.TrainingDay> trainingDays) {
        log.info("Attempt to save sessions for training {} {}}", parentTrainingEntity, trainingDays);
        ArrayList<TrainingDayEntity> savedRecords = new ArrayList<>();
        for (int order = 0; order < trainingDays.size(); order++) {
            Training.TrainingDay trainingDay = trainingDays.get(order);
            TrainingDayEntity savedDay = postgresTrainingDayRepository.save(trainingDayDomainEntityConverter.toEntity(parentTrainingEntity, trainingDay, order));
            savedRecords.add(savedDay);
        }
        log.info("Sessions saved {}", savedRecords);
        return savedRecords;
    }
}
