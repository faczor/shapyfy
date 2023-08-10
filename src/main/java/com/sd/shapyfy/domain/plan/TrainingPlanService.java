package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration;
import com.sd.shapyfy.domain.user.model.UserId;

public interface TrainingPlanService {
    TrainingConfiguration create(PlanConfiguration configurationParams, UserId userId);
}
