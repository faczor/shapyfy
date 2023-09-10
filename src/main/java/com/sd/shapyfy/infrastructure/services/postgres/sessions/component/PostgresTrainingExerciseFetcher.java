package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.configuration.model.TrainingExerciseId;
import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.exercise.TrainingExerciseFetcher;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.converter.SessionExerciseToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingExerciseFetcher implements TrainingExerciseFetcher {

    private final PostgresTrainingDayFetcher postgresTrainingDayFetcher;

    private final PostgresSessionExerciseRepository sessionExerciseRepository;

    private final SessionExerciseToDomainConverter sessionExerciseToDomainConverter;

    @Override
    public TrainingExercise fetchFor(SessionPartId sessionPartId, ExerciseId exerciseId) {
        log.info("Fetch {} {}", exerciseId, sessionPartId);
        SessionPart sessionPart = postgresTrainingDayFetcher.fetchFor(sessionPartId);

        //TODO proper exception
        return sessionPart.exercises().stream().filter(e -> e.exercise().id().equals(exerciseId)).findFirst().orElseThrow();
    }

    @Override
    public List<TrainingExercise> fetchAllFinished(ExerciseId exerciseId, UserId userId) {
        log.info("Fetch all finished {} {}", exerciseId, userId);
        List<SessionExerciseEntity> sessionExercises = sessionExerciseRepository.fetchAllFinished(exerciseId.getValue(), userId.getValue());

        return sessionExercises.stream().map(sessionExerciseToDomainConverter::convert).toList();
    }

    public SessionExerciseEntity fetchById(TrainingExerciseId id) {
        log.info("Attempt to fetch training exercise {}", id);
        return sessionExerciseRepository.findById(id.getValue()).orElseThrow();
    }
}
