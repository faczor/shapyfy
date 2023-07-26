package com.sd.shapyfy.infrastructure.services.postgres.v2.converter;

import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sd.shapyfy.domain.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.model.ConfigurationDayType.TRAINING;

@Component
@RequiredArgsConstructor
public class TrainingDayToDomainConverter {

    public ConfigurationDay toConfiguration(TrainingDayEntity trainingDayEntity) {
        return new ConfigurationDay(
                ConfigurationDayId.of(trainingDayEntity.getId()),
                trainingDayEntity.isOff() ? REST : TRAINING,
                trainingDayEntity.getName(),
                List.of() //TODO Convert exercises
        );
    }
}
