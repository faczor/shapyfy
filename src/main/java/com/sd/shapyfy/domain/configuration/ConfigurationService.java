package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.List;

public interface ConfigurationService {

    void createSession(PlanId planId, List<EditParams> editParams);

    ConfigurationDay updateOrCreateFutureSessionConfiguration(EditTargetQuery query,
                                                              EditParams editParams);


    record EditTargetQuery(
            PlanId planId,
            ConfigurationDayId configurationDayId,
            SessionState state) {
    }

    record EditParams(
            ConfigurationDayId configurationDayId,
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
