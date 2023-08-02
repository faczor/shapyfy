package com.sd.shapyfy.infrastructure.services.postgres.sessions;

import com.sd.shapyfy.domain.model.exception.SessionNotFound;
import com.sd.shapyfy.domain.TrainingPort;
import com.sd.shapyfy.domain.session.Session;
import com.sd.shapyfy.domain.session.SessionExerciseId;
import com.sd.shapyfy.domain.session.SessionId;
import com.sd.shapyfy.domain.session.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresSessionExerciseRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresTrainingDayRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.sd.shapyfy.domain.session.SessionState.FOLLOW_UP;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionsService {

    private final PostgresSessionRepository sessionRepository;

    private final PostgresTrainingDayRepository trainingDayRepository;

    private final PostgresSessionExerciseRepository sessionExerciseRepository;

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    public SessionEntity activateSession(TrainingPort.ActivateSession activateSession) {
        SessionEntity sessionEntity = sessionRepository.findById(activateSession.sessionId().getValue())
                .orElseThrow(() -> new SessionNotFound(String.format("Session not found %s", activateSession.sessionId())));

        sessionEntity.setState(SessionState.ACTIVE);
        sessionEntity.setDate(activateSession.date());

        return sessionRepository.save(sessionEntity);
    }

    public SessionEntity createFollowUpSession(TrainingPort.FollowUpTrainingSession followUpTrainingSession) {
        log.info("Creating follow up session for {}", followUpTrainingSession);
        try {
            TrainingDayEntity trainingDay = trainingDayRepository.findById(followUpTrainingSession.trainingDayId().getValue())
                    .orElseThrow(() -> new TrainingDayNotFound(String.format("Training day not found %s", followUpTrainingSession.trainingDayId())));

            SessionEntity mostActiveSession = trainingDay.getSessions().stream()
                    .filter(session -> session.getState().isFuture()).findFirst()
                    .orElseThrow();

            SessionEntity followUpSession = sessionRepository.save(new SessionEntity(
                    null,
                    FOLLOW_UP,
                    followUpTrainingSession.date(),
                    trainingDay,
                    List.of()));

            copyExercisesFromMostActiveSession(mostActiveSession, followUpSession);

            return followUpSession;
        } catch (Exception e) {
            log.error("Error creating follow up session for {}", followUpTrainingSession, e);
            throw e;
        }
    }

    public void runSession(SessionId id) {
        //TODO create method
        SessionEntity sessionEntity = sessionRepository.findById(id.getValue()).orElseThrow();
        sessionEntity.setState(SessionState.RUNNING);
        sessionRepository.save(sessionEntity);
    }

    private void copyExercisesFromMostActiveSession(SessionEntity mostActiveSession, SessionEntity followUpSession) {
        mostActiveSession.getSessionExercises().stream().map(sessionExercise -> {
            SessionExerciseEntity sessionExerciseEntity = new SessionExerciseEntity();
            sessionExerciseEntity.setExercise(sessionExercise.getExercise());
            sessionExerciseEntity.setRepsAmount(sessionExercise.getRepsAmount());
            sessionExerciseEntity.setSetsAmount(sessionExercise.getSetsAmount());
            sessionExerciseEntity.setWeightAmount(sessionExercise.getWeightAmount());
            sessionExerciseEntity.setSession(followUpSession);
            return sessionExerciseEntity;
        }).forEachOrdered(sessionExerciseRepository::save);
    }

    //TODO move method
    public void updateExercises(SessionExerciseId exerciseId, boolean isFinished, TrainingPort.ExerciseAttributes exerciseAttributes) {
        SessionExerciseEntity sessionExercise = sessionExerciseRepository.findById(exerciseId.getValue()).orElseThrow();
        sessionExercise.setFinished(isFinished);
        Optional.ofNullable(exerciseAttributes.weight()).ifPresent(sessionExercise::setWeightAmount);
        sessionExerciseRepository.save(sessionExercise);
    }

    //TODO exception move
    public Session fetch(SessionId sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId.getValue()).orElseThrow();
        return sessionEntityToDomainConverter.convert(sessionEntity);
    }

    public void finishSession(SessionId sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId.getValue()).orElseThrow();
        sessionEntity.setState(SessionState.FINISHED);
        sessionRepository.save(sessionEntity);
    }
}
