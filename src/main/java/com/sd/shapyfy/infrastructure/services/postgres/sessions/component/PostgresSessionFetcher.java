package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.session.SessionFetcher;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresSessionFetcher implements SessionFetcher {

    private final PostgresSessionRepository sessionRepository;

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    @Override
    public Session fetchBy(SessionId id) {
        log.info("Fetch by {}", id);
        SessionEntity sessionEntity = findById(id);
        return sessionEntityToDomainConverter.convert(sessionEntity);
    }

    private SessionEntity findById(SessionId id) {
        return sessionRepository.findById(id.getValue())
                .orElseThrow(); //TODO
    }
}
