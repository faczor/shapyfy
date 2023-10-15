package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.Training;

public interface TrainingPlanFetcher {

    Training fetchForSession(SessionId sessionId);
}
