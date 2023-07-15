package com.sd.shapyfy.boundary.api.trainings.converter;

import com.sd.shapyfy.boundary.api.trainings.contract.CreateTrainingDocument;
import com.sd.shapyfy.domain.TrainingManagementAdapter;
import com.sd.shapyfy.domain.TrainingManagementAdapter.TrainingInitialConfiguration.SessionDayConfiguration;
import org.springframework.stereotype.Component;

@Component
public class TrainingToDomainConverter {

    public TrainingManagementAdapter.TrainingInitialConfiguration convertForCreation(CreateTrainingDocument document) {
        return TrainingManagementAdapter.TrainingInitialConfiguration.of(
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
