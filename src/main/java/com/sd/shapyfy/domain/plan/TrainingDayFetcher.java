package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;

public interface TrainingDayFetcher {
    SessionPart fetchFor(SessionPartId sessionPartId);
}
