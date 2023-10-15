package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.session.SessionFetcher;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.PostgresTrainingRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresSessionFetcher implements SessionFetcher {

    private final PostgresSessionRepository sessionRepository;

    private final PostgresTrainingRepository trainingRepository;

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    @Override
    public Session fetchBy(SessionId id) {
        log.info("Fetch by {}", id);
        SessionEntity sessionEntity = findById(id);
        return sessionEntityToDomainConverter.convert(sessionEntity);
    }

    @Override
    public List<Session> fetchAllBy(UserId userId) {
        List<TrainingEntity> allUserTrainings = trainingRepository.findAllByUserId(userId.getValue());
        return allUserTrainings.stream()
                .map(TrainingEntity::getSessions)
                .flatMap(List::stream)
                .map(sessionEntityToDomainConverter::convert).toList();

    }

    public SessionEntity findById(SessionId id) {
        return sessionRepository.findById(id.getValue())
                .orElseThrow(); //TODO
    }
}
