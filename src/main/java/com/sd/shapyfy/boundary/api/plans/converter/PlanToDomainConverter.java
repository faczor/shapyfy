package com.sd.shapyfy.boundary.api.plans.converter;

import com.sd.shapyfy.boundary.api.plans.contract.CreatePlanDocument;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanCreationInitialConfigurationParams.SessionDayConfiguration;
import org.springframework.stereotype.Component;

@Component
public class PlanToDomainConverter {

    public TrainingPlanCreator.PlanCreationInitialConfigurationParams convertForCreation(CreatePlanDocument document) {
        return new TrainingPlanCreator.PlanCreationInitialConfigurationParams(
                document.name(),
                document.dayConfigurations().stream()
                        .map(dayConfiguration -> new SessionDayConfiguration(dayConfiguration.name(), dayConfiguration.type()))
                        .toList());
    }
}
