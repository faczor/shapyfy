package com.sd.shapyfy.domain.configuration.event;

import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Value
public class OnTrainingActivationEvent extends ApplicationEvent {

    TrainingConfiguration trainingConfiguration;

    LocalDate lastTrainingDate;

    public OnTrainingActivationEvent(Object source, TrainingConfiguration trainingConfiguration, LocalDate lastTrainingDate) {
        super(source);
        this.trainingConfiguration = trainingConfiguration;
        this.lastTrainingDate = lastTrainingDate;
    }
}

