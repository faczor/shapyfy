package com.sd.shapyfy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.model.*;

import java.time.LocalDate;
import java.util.List;

public interface TrainingPort {

    Training create(Training training);

    Training fetchFor(TrainingDayId trainingDayId);

    Training fetchFor(TrainingId trainingId);

    void updateTrainingSessions(List<ActivateSession> sessionToActivate);

    void createFollowUpSessions(List<FollowUpTrainingSession> followUpTrainingSession);

    Session runSession(UserId userId, LocalDate localDate);

    void finishExercise(SessionExerciseId exerciseId, ExerciseAttributes exerciseAttributes);

    void finishTrainingSession(SessionId sessionId);

    record ActivateSession(
            SessionId sessionId,
            LocalDate date
    ) {
    }

    record FollowUpTrainingSession(
            TrainingDayId trainingDayId,
            LocalDate date
    ) {
    }

    record ExerciseAttributes(Double weight) {
    }
}
