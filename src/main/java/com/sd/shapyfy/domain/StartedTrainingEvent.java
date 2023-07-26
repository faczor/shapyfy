package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.Plan;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class StartedTrainingEvent extends ApplicationEvent {

    Plan plan;

    LocalDate lastTrainingDate;

    public StartedTrainingEvent(Object source, Plan plan, LocalDate lastTrainingDate) {
        super(source);
        this.plan = plan;
        this.lastTrainingDate = lastTrainingDate;
    }
}
