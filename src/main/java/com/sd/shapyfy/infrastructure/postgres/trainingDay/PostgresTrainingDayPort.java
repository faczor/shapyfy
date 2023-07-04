package com.sd.shapyfy.infrastructure.postgres.trainingDay;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.domain.trainingDay.TrainingDaysAdapter;
import com.sd.shapyfy.domain.trainingDay.TrainingDaysPort;
import com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.converter.TrainingDayToEntityConverter;
import com.sd.shapyfy.infrastructure.postgres.exercises.ExerciseEntity;
import com.sd.shapyfy.infrastructure.postgres.exercises.PostgresExercisePort;
import com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.PostgresTrainingDayExerciseRepository;
import com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.TrainingDayExerciseEntity;
import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingDayPort implements TrainingDaysPort {

    private final PostgresTrainingDayRepository postgresTrainingDayRepository;

    private final TrainingDayToEntityConverter trainingDayDomainEntityConverter;

    private final PostgresTrainingDayExerciseRepository dayExerciseRepository;

    private final TrainingDayEntityToDomainConverter trainingDayEntityToDomainConverter;

    private final PostgresExercisePort exercisePort;

    @Override
    @Transactional
    public Training.TrainingDay selectExercises(TrainingDayId trainingDayId, List<TrainingDaysAdapter.ExerciseDetails> exerciseDetails) {
        log.info("{} select {}", trainingDayId, exerciseDetails);
        TrainingDayEntity trainingDay = postgresTrainingDayRepository.findById(trainingDayId.getValue())
                .orElseThrow(() -> new TrainingDayNotFound(trainingDayId.toString()));

        //forEach or save and return list
        for (int order = 0; order < exerciseDetails.size(); order++) {
            TrainingDayExerciseEntity trainingDayExercise = buildTrainingDayExerciseEntity(exerciseDetails.get(order), trainingDay, order);
            TrainingDayExerciseEntity savedEntity = dayExerciseRepository.save(trainingDayExercise);
            trainingDay.getTrainingDayExercise().add(savedEntity);
        }

        return trainingDayEntityToDomainConverter.convert(trainingDay);
    }

    private TrainingDayExerciseEntity buildTrainingDayExerciseEntity(TrainingDaysAdapter.ExerciseDetails exerciseDetails, TrainingDayEntity trainingDay, int order) {
        ExerciseEntity exercise = exercisePort.fetchFor(exerciseDetails.getExerciseId().getValue());

        return new TrainingDayExerciseEntity(
            null,
                exerciseDetails.getSets(),
                exerciseDetails.getReps(),
                exerciseDetails.getWeight().orElse(null),
                order,
                trainingDay,
                exercise
        );
    }

    @Transactional
    public ArrayList<TrainingDayEntity> createSessionsFor(TrainingEntity parentTrainingEntity, List<Training.TrainingDay> trainingDays) {
        log.info("Attempt to save sessions for training {} {}}", parentTrainingEntity, trainingDays);
        ArrayList<TrainingDayEntity> savedRecords = new ArrayList<>();
        for (int order = 0; order < trainingDays.size(); order++) {
            Training.TrainingDay trainingDay = trainingDays.get(order);
            TrainingDayEntity savedDay = postgresTrainingDayRepository.save(trainingDayDomainEntityConverter.convert(parentTrainingEntity, trainingDay, order));
            savedRecords.add(savedDay);
        }
        log.info("Sessions saved {}", savedRecords);
        return savedRecords;
    }
}
