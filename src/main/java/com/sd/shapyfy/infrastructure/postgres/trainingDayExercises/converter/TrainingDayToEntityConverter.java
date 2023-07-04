package com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.converter;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.infrastructure.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

import static com.sd.shapyfy.domain.trainingDay.TrainingDayType.OFF;

@Component
public class TrainingDayToEntityConverter {

    public TrainingDayEntity convert(TrainingEntity parentTrainingEntity, Training.TrainingDay trainingDay, int order) {
        return new TrainingDayEntity(
                null,
                Optional.ofNullable(trainingDay.getName()).orElse("REST_DAY"),
                trainingDay.getDay(),
                order,
                trainingDay.getDayType() == OFF,
                parentTrainingEntity,
                new ArrayList<>()
        );
    }
}
