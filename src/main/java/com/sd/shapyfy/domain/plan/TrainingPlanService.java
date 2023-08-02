package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.user.model.UserId;

public interface TrainingPlanService {
    PlanConfiguration create(TrainingPlanCreator.PlanCreationInitialConfigurationParams configurationParams, UserId userId);
}
