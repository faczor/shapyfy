package com.sd.shapyfy.infrastructure.services.postgres.trainings;

import com.sd.shapyfy.domain.TrainingLookup;
import com.sd.shapyfy.domain.model.*;
import com.sd.shapyfy.domain.TrainingPort;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionsService;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingToEntityConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresTrainingDayPort;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingEntityToDomainConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

//TODO - refactor to use TrainingPort - split into parts
@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingPort implements TrainingPort {

    private final TrainingToEntityConverter trainingDomainEntityConverter;

    private final PostgresTrainingDayPort trainingDayPort;

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    //TODO SessionsService?? - to be removed Port should be used...
    private final SessionsService sessionsService;

    private final TrainingLookup trainingLookup;

    @Override
    public Training create(Training training) {
        log.info("Saving training {} to postgres repository", training);
        TrainingEntity savedEntity = trainingRepository.save(trainingDomainEntityConverter.convert(training));
        List<TrainingDayEntity> sessionsCreated = trainingDayPort.createDays(savedEntity, training.getTrainingDays());
        savedEntity.setDays(sessionsCreated);
        return trainingEntityToDomainConverter.convert(savedEntity);
    }

    @Override
    public Training fetchFor(TrainingDayId trainingDayId) {
        log.info("Attempt to fetch training for day with {}", trainingDayId);
        TrainingEntity trainingEntity = trainingRepository.findByDaysId(trainingDayId.getValue())
                .orElseThrow(() -> new TrainingNotFound("Training not found for " + trainingDayId));

        return trainingEntityToDomainConverter.convert(trainingEntity);
    }

    @Override
    public Training fetchFor(PlanId planId) {
        log.info("Attempt to fetch training for {}", planId);
        TrainingEntity trainingEntity = trainingRepository.findById(planId.getValue())
                .orElseThrow(() -> new TrainingNotFound("Training not found for " + planId));

        return trainingEntityToDomainConverter.convert(trainingEntity);
    }

    //TODO Should be in fking domain...
    @Override
    public void updateTrainingSessions(List<ActivateSession> sessionToActivate) {
        log.info("Attempt to activate {}", sessionToActivate);
        sessionToActivate.forEach(sessionsService::activateSession);
    }

    @Override
    public void createFollowUpSessions(List<FollowUpTrainingSession> followUpTrainingSessions) {
        log.info("Attempt to create follow up sessions by {}", followUpTrainingSessions);
        followUpTrainingSessions.forEach(sessionsService::createFollowUpSession);
    }

    @Override
    public Session runSession(UserId userId, LocalDate localDate) {
        log.info("Attempt to run session for {} on {}", userId, localDate);
        TrainingLookup.CurrentTraining currentTraining = trainingLookup.currentTrainingFor(userId);
        Session session = currentTraining.sessionFor(localDate);
        sessionsService.runSession(session.getId());

        return session;
    }

    @Override
    public void finishExercise(SessionExerciseId exerciseId, ExerciseAttributes exerciseAttributes) {
        log.info("Attempt to finish exercise {} with {}", exerciseId, exerciseAttributes);
        sessionsService.updateExercises(exerciseId, true, exerciseAttributes);
    }

    @Override
    public void finishTrainingSession(SessionId sessionId) {
        log.info("Attempt to finish session {}", sessionId);
        Session session = sessionsService.fetch(sessionId);
        if (FALSE.equals(session.isRunning())) {
            throw new RuntimeException(); //TODO return proper exception ;)
        }
        List<Session.SessionExercise> notFinishedExercises = session.notFinishedExercises();
        if (isNotEmpty(notFinishedExercises)) {
            throw new RuntimeException(); //TODO return proper exception ;)
        }

        sessionsService.finishSession(sessionId);
    }

}
