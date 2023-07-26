package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.model.UserId;

public interface TrainingPlanService {
    PlanConfiguration create(TrainingPlanCreator.PlanCreationInitialConfigurationParams configurationParams, UserId userId);
}
