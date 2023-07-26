package com.sd.shapyfy.infrastructure.services.postgres.sessions.converter;

import com.sd.shapyfy.domain.session.Session;
import com.sd.shapyfy.domain.session.SessionId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionEntityToDomainConverter {

    private final SessionExerciseEntityToDomainConverter sessionExerciseEntityToDomainConverter;

    public Session convert(SessionEntity session) {
        return new Session(
                SessionId.of(session.getId()),
                session.getState(),
                session.getDate(),
                session.getSessionExercises().stream().map(sessionExerciseEntityToDomainConverter::convert).toList()
        );
    }
}
