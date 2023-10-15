package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.model.ConfigurationAttributeId;
import com.sd.shapyfy.domain.configuration.model.ConfigurationId;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SessionService {

    Session createSession(CreateSessionRequestParams createSessionRequestParams);

    Session updateStatus(SessionId sessionId, SessionState state);

    record CreateSessionRequestParams(
            ConfigurationId configurationId,
            SessionState state,
            List<CreateSessionPartRequestParams> createSessionPartRequestParams) {

        public record CreateSessionPartRequestParams(
                ConfigurationDayId configurationPartId,
                String name,
                LocalDate date,
                SessionPartType type,
                SessionPartState state,
                List<CreateTrainingExerciseRequestParams> createTrainingExerciseRequestParams,
                List<CreateAttributeRequestParams> createAttributeRequestParams) {

            public record CreateTrainingExerciseRequestParams(
                    ExerciseId exerciseId,
                    int sets,
                    int reps,
                    int breakBetweenSets,
                    Double weight) {
            }

            public record CreateAttributeRequestParams(
                    ConfigurationAttributeId attributeId) {
            }
        }
    }
}
