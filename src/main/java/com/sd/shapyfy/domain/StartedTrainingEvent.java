package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.Training;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class StartedTrainingEvent extends ApplicationEvent {

    Training training;

    LocalDate lastTrainingDate;

    public StartedTrainingEvent(Object source, Training training, LocalDate lastTrainingDate) {
        super(source);
        this.training = training;
        this.lastTrainingDate = lastTrainingDate;
    }
}
