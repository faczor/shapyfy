package com.sd.shapyfy.domain.configuration;


import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;

//TODO rething naming maybe PlanConfigurationFetcher
public interface TrainingFetcher {

    PlanConfiguration trainingConfigurationBy(PlanId planId);
}
