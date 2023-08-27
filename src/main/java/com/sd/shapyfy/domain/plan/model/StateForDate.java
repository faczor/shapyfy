package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;

import java.time.LocalDate;

public record StateForDate(
        LocalDate date,
        boolean isSessionExisting,
        boolean isTrainingDay,
        Session session,
        ConfigurationDay configurationDay) {

    public static StateForDate noTraining(LocalDate date) {
        return new StateForDate(
                date,
                false,
                false,
                null,
                null
        );
    }
}
