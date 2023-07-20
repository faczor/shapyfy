package com.sd.shapyfy.boundary.api.plans.converter;

import com.sd.shapyfy.boundary.api.plans.contract.CreatePlanDocument;
import com.sd.shapyfy.domain.PlanManagementAdapter;
import com.sd.shapyfy.domain.PlanManagementAdapter.TrainingInitialConfiguration.SessionDayConfiguration;
import org.springframework.stereotype.Component;

@Component
public class PlanToDomainConverter {

    public PlanManagementAdapter.TrainingInitialConfiguration convertForCreation(CreatePlanDocument document) {
        return PlanManagementAdapter.TrainingInitialConfiguration.of(
                document.name(),
                document.dayConfigurations().stream()
                        .map(dayConfiguration -> SessionDayConfiguration.of(
                                dayConfiguration.name(),
                                dayConfiguration.dayOfWeek(),
                                dayConfiguration.type()
                                )
                        ).toList());
    }
}
