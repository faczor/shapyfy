package com.sd.shapyfy.boundary.api.trainings.converter;

import com.sd.shapyfy.boundary.api.trainings.contract.CreateTrainingDocument;
import com.sd.shapyfy.domain.training.TrainingAdapter;
import com.sd.shapyfy.domain.training.TrainingAdapter.TrainingInitialConfiguration.SessionDayConfiguration;
import org.springframework.stereotype.Component;

@Component
public class TrainingToDomainConverter {

    public TrainingAdapter.TrainingInitialConfiguration convertForCreation(CreateTrainingDocument document) {
        return TrainingAdapter.TrainingInitialConfiguration.of(
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
