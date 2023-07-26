package com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter;

import com.sd.shapyfy.domain.model.TrainingDay;
import com.sd.shapyfy.domain.model.TrainingDayId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sd.shapyfy.domain.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.model.ConfigurationDayType.TRAINING;

@Component
@RequiredArgsConstructor
public class TrainingDayEntityToDomainConverter {

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    public TrainingDay convert(TrainingDayEntity entity) {
        return new TrainingDay(
                TrainingDayId.of(entity.getId()),
                entity.getName(),
                entity.getDay(),
                entity.isOff() ? REST : TRAINING,
                entity.getSessions().stream().map(sessionEntityToDomainConverter::convert).toList()
        );
    }
}