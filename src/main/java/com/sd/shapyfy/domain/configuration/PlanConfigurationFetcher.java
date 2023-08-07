package com.sd.shapyfy.domain.configuration;


import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.domain.user.model.UserId;

import java.util.List;

public interface PlanConfigurationFetcher {

    PlanConfiguration trainingConfigurationBy(PlanId planId);

    List<Training> fetchAllTrainingsFor(UserId userId);
}
