package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;

import java.time.LocalDate;

public record StateForDate(
        LocalDate date,
        Training training,
        boolean isSessionExisting,
        boolean isTrainingDay,
        SessionPart sessionPart,
        ConfigurationDay configurationDay) {

    public static StateForDate noTraining(LocalDate date) {
        return new StateForDate(
                date,
                null,
                false,
                false,
                null,
                null
        );
    }
}
