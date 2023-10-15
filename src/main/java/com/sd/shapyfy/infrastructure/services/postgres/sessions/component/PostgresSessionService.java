package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.configuration.SessionService;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.PostgresConfigurationRepository;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.CreationParamsConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.PostgresTrainingPlanService;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresSessionService implements SessionService {


    private final PostgresSessionRepository sessionRepository;

    private final PostgresTrainingPlanService trainingPlanService;

    private final PostgresConfigurationRepository configurationRepository;

    private final CreationParamsConverter creationParamsConverter;

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    @Override
    public Session createSession(CreateSessionRequestParams params) {
        log.info("Create session with params {}", params);
        ConfigurationEntity configurationEntity = configurationRepository.findById(params.configurationId().getValue())
                //TODO proper exception
                .orElseThrow();
        TrainingEntity training = configurationEntity.getTraining();
        SessionEntity createdSession = training.createSession(params.state(), creationParamsConverter.convert(params));

        trainingPlanService.save(training);

        return sessionEntityToDomainConverter.convert(createdSession);
    }

    @Override
    public Session updateStatus(SessionId sessionId, SessionState state) {
        log.info("Update session {} with {}", sessionId, state);
        SessionEntity sessionEntity = getById(sessionId);
        sessionEntity.setState(state);
        sessionRepository.save(sessionEntity);

        return sessionEntityToDomainConverter.convert(sessionEntity);
    }

    private SessionEntity getById(SessionId sessionId) {
        return sessionRepository.findById(sessionId.getValue())
                //TODO proper exception
                .orElseThrow();
    }
}
