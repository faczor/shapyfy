package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingLookup {

    private final PlanConfigurationFetcher planConfigurationFetcher;

    public PlanConfiguration configurationFor(PlanId planId) {
        return planConfigurationFetcher.trainingConfigurationBy(planId);
    }

}
