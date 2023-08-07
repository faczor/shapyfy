package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.DateRange;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;

import java.time.LocalDate;
import java.util.*;

public record Training(
        PlanConfiguration configuration,
        List<Session> sessions) {

    public StateForDate stateFor(LocalDate date) {
        Optional<Session> possibleSession = sessions().stream().filter(session -> session.dateRange().isRangeContaining(date)).findFirst();
        if (possibleSession.isPresent()) {
            return stateFromSession(possibleSession.get(), date);
        }

        LocalDate lastSessionDate = getLastSessionDate();
        if (lastSessionDate == null) {
            return StateForDate.noTraining(date);
        }

        return date.isAfter(lastSessionDate)
                ? futureState(lastSessionDate, date)
                : StateForDate.noTraining(date);

    }

    private StateForDate futureState(LocalDate lastSessionDate, LocalDate searchDate) {
        LocalDate startDateOfSessionForDate = startDateOfSessionForDate(lastSessionDate, searchDate);
        List<LocalDate> datesWithinRange = new DateRange(startDateOfSessionForDate, searchDate).datesWithinRange().toList();
        for (int i = 0; i < datesWithinRange.size(); i++) {
            if (datesWithinRange.get(i).equals(searchDate)) {
                ConfigurationDay configurationDay = configuration().configurationDays().get(i);
                return new StateForDate(
                        searchDate,
                        false,
                        configurationDay.isTrainingDay(),
                        configurationDay);
            }
        }
        throw new IllegalStateException();
    }

    private StateForDate stateFromSession(Session session, LocalDate date) {
        return session.sessionParts().stream().filter(part -> part.date().equals(date)).findFirst()
                .map(part -> new StateForDate(part.date(), true, true, configuration.configurationDays().stream().filter(configDay -> configDay.id().equals(part.configurationDayId())).findFirst().orElseThrow()))
                .orElse(new StateForDate(date, true, false, null));
    }

    private LocalDate getLastSessionDate() {
        try {
            return Collections.max(sessions(), Comparator.comparing(Session::lastDate)).dateRange().end();
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate startDateOfSessionForDate(LocalDate lastSessionDate, LocalDate searchDate) {
        while (true) {
            DateRange nextSessionRange = nextSession(lastSessionDate);
            if (nextSessionRange.isRangeContaining(searchDate)) {
                return nextSessionRange.start();
            }
            lastSessionDate = nextSessionRange.end();
        }
    }

    private DateRange nextSession(LocalDate date) {
        return new DateRange(date.plusDays(1), date.plusDays(configuration().daysPlanAmount()));
    }
}
