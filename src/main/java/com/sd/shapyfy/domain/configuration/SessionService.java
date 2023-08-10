package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.List;

public interface SessionService {

    void createSession(PlanId planId, EditSessionParams editSessionPartParams);

    void updateSession(SessionId sessionId, EditSessionParams editSessionParams);


    record EditSessionParams(
            SessionState state,
            List<EditSessionPart> editSessionPart) {

        public record EditSessionPart(
                SessionPartId sessionPartId,
                EditSessionPartParams editSessionPartParams
        ) {
        }
    }

    record EditSessionPartParams(
            String name,
            SessionPartType type,
            LocalDate date,
            SessionPartState state,
            List<SessionExerciseExerciseEditableParam> sessionExerciseExerciseEditableParam
    ) {
        public record SessionExerciseExerciseEditableParam(
                ExerciseId exerciseId,
                Integer setsAmount,
                Integer repsAmount,
                Double weightAmount,
                Integer restBetweenSets,
                Boolean isFinished) {
        }
    }
}
