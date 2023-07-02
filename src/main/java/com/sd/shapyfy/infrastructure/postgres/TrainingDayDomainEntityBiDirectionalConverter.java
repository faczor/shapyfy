package com.sd.shapyfy.infrastructure.postgres;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.infrastructure.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.sd.shapyfy.domain.trainingDay.TrainingDayType.OFF;
import static com.sd.shapyfy.domain.trainingDay.TrainingDayType.TRAINING;
import static java.util.Comparator.comparing;

@Component
public class TrainingDayDomainEntityBiDirectionalConverter {

    public TrainingDayEntity toEntity(TrainingEntity parentTrainingEntity, Training.TrainingDay trainingDay, int order) {
        return new TrainingDayEntity(
                null,
                Optional.ofNullable(trainingDay.getName()).orElse("REST_DAY"),
                trainingDay.getDay(),
                order,
                trainingDay.getDayType() == OFF,
                parentTrainingEntity
        );
    }
    

    public List<Training.TrainingDay> toDomain(List<TrainingDayEntity> trainingDayEntity) {
        return trainingDayEntity.stream().sorted(comparing(TrainingDayEntity::getOrder))
                .map(day -> new Training.TrainingDay(
                                TrainingDayId.of(day.getId()),
                                day.getName(),
                                day.getDay(),
                                day.isOff() ? OFF : TRAINING
                        )
                ).toList();
    }
}
