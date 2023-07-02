package com.sd.shapyfy.infrastructure.postgres;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingId;
import com.sd.shapyfy.domain.user.UserId;
import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class TrainingDomainEntityBiDirectionalConverter {

    private final TrainingDayDomainEntityBiDirectionalConverter trainingDayDomainEntityConverter;

    public TrainingEntity toEntity(Training training) {
        return new TrainingEntity(
                Optional.ofNullable(training.getId()).map(TrainingId::getValue).orElse(null),
                training.getUserId().getValue(),
                training.getName(),
                new ArrayList<>()
        );
    }

    public Training toDomain(TrainingEntity entity) {
        return new Training(
                TrainingId.of(entity.getId()),
                UserId.of(entity.getUserId()),
                entity.getName(),
                trainingDayDomainEntityConverter.toDomain(entity.getDays())
        );
    }
}
