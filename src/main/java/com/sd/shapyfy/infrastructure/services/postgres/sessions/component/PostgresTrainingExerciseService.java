package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;


import com.sd.shapyfy.domain.plan.TrainingExerciseService;
import com.sd.shapyfy.domain.plan.TrainingProcess;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.converter.SessionExerciseToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresTrainingExerciseService implements TrainingExerciseService {

    private final PostgresTrainingExerciseFetcher trainingExerciseFetcher;

    private final PostgresSessionExerciseRepository sessionExerciseRepository;

    private final SessionExerciseToDomainConverter sessionExerciseToDomainConverter;

    @Override
    public TrainingExercise update(TrainingProcess.UpdateTrainingExercise updateTrainingExercise) {
        log.info("Attempt to update trainingExercise {}", updateTrainingExercise);
        SessionExerciseEntity sessionExerciseEntity = trainingExerciseFetcher.fetchById(updateTrainingExercise.id());
        sessionExerciseEntity.update(
                updateTrainingExercise.updateAttributeRequests(), //TODO need converter
                updateTrainingExercise.updateSetRequests(), //TODO need converter
                updateTrainingExercise.isFinished());

        sessionExerciseRepository.save(sessionExerciseEntity);
        return sessionExerciseToDomainConverter.convert(sessionExerciseEntity);
    }
}
