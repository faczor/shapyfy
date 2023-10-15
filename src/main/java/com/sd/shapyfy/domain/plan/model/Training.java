package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.DateRange;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record Training(
        TrainingConfiguration configuration,
        List<Session> sessions) {

    //TODO move to converter :)
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

    public boolean isActive() {
        return sessions().stream().anyMatch(Session::isActive);
    }

    public Session getFollowUpSession() {
        return sessions().stream().filter(session -> session.state() == SessionState.FOLLOW_UP)
                .findFirst().orElseThrow();
    }

    private StateForDate futureState(LocalDate lastSessionDate, LocalDate searchDate) {
        LocalDate startDateOfSessionForDate = startDateOfSessionForDate(lastSessionDate, searchDate);
        List<LocalDate> datesWithinRange = new DateRange(startDateOfSessionForDate, searchDate).datesWithinRange().toList();
        for (int i = 0; i < datesWithinRange.size(); i++) {
            if (datesWithinRange.get(i).equals(searchDate)) {
                ConfigurationDay configurationDay = configuration().configurationDays().get(i);
                return new StateForDate(
                        searchDate,
                        this,
                        false,
                        configurationDay.isTrainingDay(),
                        null,
                        configurationDay);
            }
        }
        throw new IllegalStateException();
    }

    private StateForDate stateFromSession(Session session, LocalDate date) {
        SessionPart sessionPart = session.partFor(date);
        return new StateForDate(
                date,
                this,
                true,
                sessionPart.type() == SessionPartType.TRAINING_DAY,
                sessionPart,
                configuration().forSessionPart(sessionPart));
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
