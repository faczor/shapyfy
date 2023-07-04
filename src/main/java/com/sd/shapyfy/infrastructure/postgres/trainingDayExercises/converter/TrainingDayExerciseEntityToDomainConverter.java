package com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.converter;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingDayExerciseId;
import com.sd.shapyfy.infrastructure.postgres.exercises.ExerciseEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.TrainingDayExerciseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingDayExerciseEntityToDomainConverter {

    private final ExerciseEntityToDomainConverter exerciseEntityToDomainConverter;

    public Training.TrainingDay.TrainingDayExercise convert(TrainingDayExerciseEntity entity) {
        return new Training.TrainingDay.TrainingDayExercise(
                TrainingDayExerciseId.of(entity.getId()),
                entity.getSetsAmount(),
                entity.getRepsAmount(),
                entity.getWeightAmount(),
                exerciseEntityToDomainConverter.convert(entity.getExercise())
        );
    }
}
