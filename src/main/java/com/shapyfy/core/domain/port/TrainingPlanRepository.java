package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;

public interface TrainingPlanRepository {

    TrainingPlan save(TrainingPlan trainingPlan);

    TrainingPlan findByUserId(UserId userId);
}
