package com.sd.shapyfy.infrastructure.postgres.trainings;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingPort;
import com.sd.shapyfy.domain.training.TrainingId;
import com.sd.shapyfy.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class PostgresTrainingPort implements TrainingPort {

    private final TrainingConverter trainingConverter;

    private final PostgresTrainingRepository trainingRepository;

    @Override
    public Training save(Training training) {
        log.info("Saving training {} to postgres repository", training);
        TrainingEntity entity = trainingConverter.toEntity(training);
        return trainingConverter.toDomain(trainingRepository.save(entity));
    }

    public static class TrainingConverter {

        private TrainingEntity toEntity(Training training) {
            return new TrainingEntity(
                    Optional.ofNullable(training.getId()).map(TrainingId::getValue).orElse(null),
                    UUID.fromString(training.getUserId().getValue()),
                    new HashSet<>()
            );
        }

        private Training toDomain(TrainingEntity trainingEntity) {
            return new Training(
                    TrainingId.of(trainingEntity.getId()),
                    UserId.of(trainingEntity.getUserId().toString())
            );
        }

    }
}
