package com.sd.shapyfy.domain.configuration.event;

import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Value
public class OnTrainingActivationEvent extends ApplicationEvent {

    TrainingConfiguration trainingConfiguration;

    ConfigurationDayId startDayId;

    LocalDate trainingStartDate;

    public OnTrainingActivationEvent(Object source, TrainingConfiguration trainingConfiguration, ConfigurationDayId startDayId, LocalDate trainingStartDate) {
        super(source);
        this.trainingConfiguration = trainingConfiguration;
        this.startDayId = startDayId;
        this.trainingStartDate = trainingStartDate;
    }
}

