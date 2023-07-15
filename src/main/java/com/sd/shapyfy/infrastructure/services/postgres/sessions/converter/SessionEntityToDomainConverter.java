package com.sd.shapyfy.infrastructure.services.postgres.sessions.converter;

import com.sd.shapyfy.domain.session.model.Session;
import com.sd.shapyfy.domain.session.model.SessionId;
import com.sd.shapyfy.domain.session.model.SessionType;
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
                SessionType.valueOf(session.getState()),
                session.getSessionExercises().stream().map(sessionExerciseEntityToDomainConverter::convert).toList()
        );
    }
}
