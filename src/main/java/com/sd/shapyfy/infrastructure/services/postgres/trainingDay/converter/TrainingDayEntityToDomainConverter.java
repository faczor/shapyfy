package com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sd.shapyfy.domain.trainingDay.TrainingDayType.OFF;
import static com.sd.shapyfy.domain.trainingDay.TrainingDayType.TRAINING;

@Component
@RequiredArgsConstructor
public class TrainingDayEntityToDomainConverter {

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    public Training.TrainingDay convert(TrainingDayEntity entity) {
        return new Training.TrainingDay(
                TrainingDayId.of(entity.getId()),
                entity.getName(),
                entity.getDay(),
                entity.isOff() ? OFF : TRAINING,
                entity.getSessions().stream().map(sessionEntityToDomainConverter::convert).toList()
        );
    }
}