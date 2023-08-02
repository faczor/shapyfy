package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.*;
import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.domain.session.Session;
import com.sd.shapyfy.domain.session.SessionExerciseId;
import com.sd.shapyfy.domain.session.SessionId;

import java.time.LocalDate;
import java.util.List;

public interface TrainingPort {

    Plan create(Plan plan);

    Plan fetchFor(TrainingDayId trainingDayId);

    Plan fetchFor(PlanId planId);

    void updateTrainingSessions(List<ActivateSession> sessionToActivate);

    void createFollowUpSessions(List<FollowUpTrainingSession> followUpTrainingSession);

    Session runSession(UserId userId, LocalDate localDate);

    void finishExercise(SessionExerciseId exerciseId, ExerciseAttributes exerciseAttributes);

    void finishTrainingSession(SessionId sessionId);

    record ActivateSession(
            SessionId sessionId,
            LocalDate date    ) {
    }

    record FollowUpTrainingSession(
            TrainingDayId trainingDayId,
            LocalDate date
    ) {
    }

    record ExerciseAttributes(Double weight) {
    }
}
