package com.shapyfy.core.util;

import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;
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
        return streamDatesWithinRange().anyMatch(dateIterator -> dateIterator.equals(date));
    }

    public Stream<LocalDate> streamDatesWithinRange() {
        return start().datesUntil(end.plusDays(1));
    }

    public List<LocalDate> listDatesWithinRange() {
        return start().datesUntil(end.plusDays(1)).toList();
    }

    public DateRange rangeAfter(int days) {
        LocalDate beggingOfNextRange = this.end().plusDays(1);
        return new DateRange(beggingOfNextRange, beggingOfNextRange.plusDays(days));
    }
}
