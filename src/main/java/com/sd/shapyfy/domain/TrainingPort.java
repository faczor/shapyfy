package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.Training;
import com.sd.shapyfy.domain.model.TrainingId;
import com.sd.shapyfy.domain.model.SessionId;
import com.sd.shapyfy.domain.model.TrainingDayId;

import java.time.LocalDate;
import java.util.List;

public interface TrainingPort {

    Training create(Training training);

    Training fetchFor(TrainingDayId trainingDayId);

    Training fetchFor(TrainingId trainingId);

    void updateTrainingSessions(List<ActivateSession> sessionToActivate);

    void createFollowUpSessions(List<FollowUpTrainingSession> followUpTrainingSession);

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
}
