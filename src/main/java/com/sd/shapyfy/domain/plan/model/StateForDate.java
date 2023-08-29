package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;

import java.time.LocalDate;

public record StateForDate(
        LocalDate date,
        PlanId planId,
        boolean isSessionExisting,
        boolean isTrainingDay,
        Session session,
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
