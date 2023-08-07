package com.sd.shapyfy.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DateRangeTest {
    static Stream<Arguments> isRangeContainingCases() {
        return Stream.of(
                Arguments.of("Should return true if date is equal start range", date(1), date(5), date(1), true),
                Arguments.of("Should return true if date is equal end range", date(1), date(5), date(5), true),
                Arguments.of("Should return true if date is between range dates", date(1), date(5), date(3), true),
                Arguments.of("Should return false if date is not within range", date(1), date(5), date(6), false)
        );
    }


    @ParameterizedTest(name = "[shouldReturnIfRangeIsContainingDate]: {0}")
    @MethodSource("isRangeContainingCases")
    public void shouldReturnIfRangeIsContainingDate(String caseName, LocalDate rangeStart, LocalDate rangeEnd, LocalDate paramDate, boolean expectedResult) {
        DateRange dateRange = new DateRange(rangeStart, rangeEnd);

        assertThat(dateRange.isRangeContaining(paramDate)).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnDateRangeAfter5Days() {
        DateRange baseRange = new DateRange(date(1), date(5));

        assertThat(baseRange.rangeAfter(5)).isEqualTo(new DateRange(date(6), date(11)));
    }

    @Test
    public void shouldReturnDatesInRangeWhenStartDateIsBeforeEndDate() {
        DateRange range = new DateRange(date(1), date(5));

        assertThat(range.datesWithinRange().toList()).containsAll(List.of(
                date(1),
                date(2),
                date(3),
                date(4),
                date(5)
        ));
    }

    @Test
    public void shouldReturnDatesInRangeWhenStartDateIsAfterEndDate() {
        DateRange range = new DateRange(date(5), date(1));

        assertThat(range.datesWithinRange().toList()).containsAll(List.of(
                date(1),
                date(2),
                date(3),
                date(4),
                date(5)
        ));
    }

    private static LocalDate date(int day) {
        return LocalDate.of(2023, 1, day);
    }
}
