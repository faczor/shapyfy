package com.sd.shapyfy.infrastructure.postgres.trainings.converter;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingId;
import com.sd.shapyfy.domain.user.UserId;
import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.converter.TrainingDayToEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class TrainingToEntityConverter {

    public TrainingEntity convert(Training training) {
        return new TrainingEntity(
                Optional.ofNullable(training.getId()).map(TrainingId::getValue).orElse(null),
                training.getUserId().getValue(),
                training.getName(),
                new ArrayList<>()
        );
    }
}
