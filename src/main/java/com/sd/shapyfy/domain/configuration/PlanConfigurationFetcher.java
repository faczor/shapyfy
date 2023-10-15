package com.sd.shapyfy.domain.configuration;


import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.domain.user.model.UserId;

import java.util.List;

public interface PlanConfigurationFetcher {

    TrainingConfiguration trainingConfigurationBy(PlanId planId);

    List<Training> fetchAllTrainingsFor(UserId userId);

    Training trainingFor(PlanId planId);

    Training fetchForSession(SessionId sessionId);
}
