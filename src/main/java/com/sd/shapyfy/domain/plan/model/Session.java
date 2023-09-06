package com.sd.shapyfy.domain.plan.model;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.domain.DateRange;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.List;

public record Session(

        SessionId sessionId,
        SessionState state,
        List<SessionPart> sessionParts) {

    public DateRange dateRange() {
        return new DateRange(
                sessionParts().get(0).date(),
                Iterables.getLast(sessionParts()).date()
        );
    }

    public LocalDate lastDate() {
        return dateRange().end();
    }

    public boolean isActive() {
        return state.isActive();
    }

    //TODO proper exception
    public SessionPart partFor(LocalDate localDate) {
        return sessionParts.stream().filter(part -> part.date().equals(localDate)).findFirst().orElseThrow();
    }

}
