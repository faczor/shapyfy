package com.sd.shapyfy.domain;


import com.sd.shapyfy.domain.model.Plan;
import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.domain.plan.PlanConfiguration;
import com.sd.shapyfy.domain.plan.PlanId;

import java.util.List;

public interface TrainingFetcher {

    List<Plan> fetchFor(UserId userId);

    PlanConfiguration trainingConfigurationBy(PlanId planId);
}
