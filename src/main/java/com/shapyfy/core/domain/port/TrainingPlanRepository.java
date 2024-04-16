package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.model.TrainingPlan;

public interface TrainingPlanRepository {

    TrainingPlan save(TrainingPlan trainingPlan);
}
