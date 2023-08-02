package com.sd.shapyfy.domain.configuration;


import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;

public interface PlanConfigurationFetcher {

    PlanConfiguration trainingConfigurationBy(PlanId planId);
}
