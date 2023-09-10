package com.sd.shapyfy.domain;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public record DateRange(
        @NonNull
        LocalDate start,
        @NonNull
        LocalDate end) {

    public DateRange(@NonNull LocalDate start,
                     @NonNull LocalDate end) {
        if (start.isAfter(end)) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
    }

    public boolean isRangeContaining(LocalDate date) {
        return datesWithinRange().anyMatch(dateIterator -> dateIterator.equals(date));
    }

    public Stream<LocalDate> datesWithinRange() {
        return start().datesUntil(end.plusDays(1));
    }

    public DateRange rangeAfter(int days) {
        LocalDate beggingOfNextRange = this.end().plusDays(1);
        return new DateRange(beggingOfNextRange, beggingOfNextRange.plusDays(days));
    }

}
