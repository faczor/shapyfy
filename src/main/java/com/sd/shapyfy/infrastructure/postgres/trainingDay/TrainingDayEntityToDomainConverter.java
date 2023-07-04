package com.sd.shapyfy.infrastructure.postgres.trainingDay;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.converter.TrainingDayExerciseEntityToDomainConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sd.shapyfy.domain.trainingDay.TrainingDayType.OFF;
import static com.sd.shapyfy.domain.trainingDay.TrainingDayType.TRAINING;

@Component
@RequiredArgsConstructor
public class TrainingDayEntityToDomainConverter {

    private final TrainingDayExerciseEntityToDomainConverter trainingDayExerciseEntityToDomainConverter;

    public Training.TrainingDay convert(TrainingDayEntity entity) {
        return new Training.TrainingDay(
                TrainingDayId.of(entity.getId()),
                entity.getName(),
                entity.getDay(),
                entity.isOff() ? OFF : TRAINING,
                entity.getTrainingDayExercise().stream().map(trainingDayExerciseEntityToDomainConverter::convert).toList()
        );
    }
}


//    public List<Training.TrainingDay> toDomain(List<TrainingDayEntity> trainingDayEntity) {
//        return trainingDayEntity.stream().sorted(comparing(TrainingDayEntity::getOrder))
//                .map(day -> new Training.TrainingDay(
//                                TrainingDayId.of(day.getId()),
//                                day.getName(),
//                                day.getDay(),
//                                day.isOff() ? OFF : TRAINING,
//                                List.of()
//                        )
//                ).toList();
//    }
//
//    public Training.TrainingDay toDomain(TrainingDayEntity day) {
//        return new Training.TrainingDay(
//                TrainingDayId.of(day.getId()),
//                day.getName(),
//                day.getDay(),
//                day.isOff() ? OFF : TRAINING,
//                List.of()
//        );
//    }