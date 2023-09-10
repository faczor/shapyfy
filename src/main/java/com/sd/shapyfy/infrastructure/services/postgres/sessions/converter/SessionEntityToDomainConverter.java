package com.sd.shapyfy.infrastructure.services.postgres.sessions.converter;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.converter.SessionExerciseToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionEntityToDomainConverter {

    private final SessionExerciseToDomainConverter sessionExerciseToDomainConverter;

    public Session convert(SessionEntity sessionEntity) {
        return new Session(
                SessionId.of(sessionEntity.getId()),
                sessionEntity.getState(),
                sessionEntity.getSessionParts().stream().map(this::convertPart).toList());
    }

    public SessionPart convertPart(SessionPartEntity partEntity) {
        return new SessionPart(
                SessionPartId.of(partEntity.getId()),
                ConfigurationDayId.of(partEntity.getConfigurationPartId()),
                partEntity.getName(),
                partEntity.getType(),
                partEntity.getState(),
                partEntity.getDate(),
                partEntity.getSessionExercises().stream().map(sessionExerciseToDomainConverter::convert).toList()
        );
    }
}
