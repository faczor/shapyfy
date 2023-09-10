package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.plan.TrainingDayFetcher;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingDayFetcher implements TrainingDayFetcher {

    private final PostgresSessionPartRepository sessionPartRepository;

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    @Override
    public SessionPart fetchFor(SessionPartId sessionPartId) {
        log.info("Fetch session part {}", sessionPartId);
        SessionPartEntity sessionPartEntity = getById(sessionPartId);

        return sessionEntityToDomainConverter.convertPart(sessionPartEntity);
    }

    //TODO proper exception :)
    private SessionPartEntity getById(SessionPartId sessionPartId) {
        return sessionPartRepository.findById(sessionPartId.getValue()).orElseThrow();
    }
}
