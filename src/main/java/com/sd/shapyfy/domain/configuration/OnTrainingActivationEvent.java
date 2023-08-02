package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Value
public class OnTrainingActivationEvent extends ApplicationEvent {

    PlanConfiguration planConfiguration;

    LocalDate lastTrainingDate;

    public OnTrainingActivationEvent(Object source, PlanConfiguration planConfiguration, LocalDate lastTrainingDate) {
        super(source);
        this.planConfiguration = planConfiguration;
        this.lastTrainingDate = lastTrainingDate;
    }
}

