package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.DateRange;

import java.time.LocalDate;
import java.util.List;

public record Session(

        SessionId sessionId,
        List<SessionPart> sessionParts,
        DateRange dateRange) {

    public LocalDate lastDate() {
        return dateRange().end();
    }
}
