package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.List;

public interface ConfigurationService {

    ConfigurationDay createSession(ConfigurationDayId configurationDayId, EditableSessionParams editableSessionParams);

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
