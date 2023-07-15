package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingId;
import com.sd.shapyfy.domain.user.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingEntityToDomainConverter {

    private final TrainingDayEntityToDomainConverter trainingDayEntityToDomainConverter;

    public Training convert(TrainingEntity entity) {
        return new Training(
                TrainingId.of(entity.getId()),
                UserId.of(entity.getUserId()),
                entity.getName(),
                entity.getDays().stream().map(trainingDayEntityToDomainConverter::convert).toList()
        );
    }
}
