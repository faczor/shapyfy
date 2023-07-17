package com.sd.shapyfy.infrastructure.services.postgres.trainings;

import com.sd.shapyfy.domain.model.Training;
import com.sd.shapyfy.domain.model.TrainingId;
import com.sd.shapyfy.domain.TrainingPort;
import com.sd.shapyfy.domain.model.TrainingDayId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionsService;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingToEntityConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresTrainingDayPort;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingEntityToDomainConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingPort implements TrainingPort {

    private final TrainingToEntityConverter trainingDomainEntityConverter;

    private final PostgresTrainingDayPort trainingDayPort;

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    private final SessionsService sessionsService;

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
    public Training fetchFor(TrainingId trainingId) {
        log.info("Attempt to fetch training for {}", trainingId);
        TrainingEntity trainingEntity = trainingRepository.findById(trainingId.getValue())
                .orElseThrow(() -> new TrainingNotFound("Training not found for " + trainingId));

        return trainingEntityToDomainConverter.convert(trainingEntity);
    }

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
}
