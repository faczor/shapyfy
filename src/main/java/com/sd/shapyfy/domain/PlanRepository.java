package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.domain.plan.PlanConfiguration;

public interface PlanRepository {
    PlanConfiguration create(PlanManagementAdapter.TrainingInitialConfiguration initialConfiguration, UserId userId);
}
