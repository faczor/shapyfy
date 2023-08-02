package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.ExerciseId;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import com.sd.shapyfy.domain.session.SessionState;

import java.time.LocalDate;
import java.util.List;

public interface SessionService {

    void createSession(ConfigurationDayId configurationDayId, EditableSessionParams editableSessionParams);

    void updateSessionWithState(ConfigurationDayId configurationDayId, SessionState state, EditableSessionParams editableSessionParams);

    record EditableSessionParams(
            SessionState state,
            LocalDate date,
            List<SessionExerciseExerciseEditableParam> sessionExerciseExerciseEditableParam
    ) {
        public record SessionExerciseExerciseEditableParam(
                ExerciseId exerciseId,
                Integer setsAmount,
                Integer repsAmount,
                Double weightAmount,
                Boolean isFinished) {
        }
    }
}
