package com.cinema.screenings.domain;

import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.ZONE_OFFSET;
import static com.cinema.screenings.ScreeningFixtures.SCREENING_DATE;
import static com.cinema.screenings.domain.ScreeningConstants.MAX_DAYS_BEFORE_SCREENING;
import static com.cinema.screenings.domain.ScreeningConstants.MIN_DAYS_BEFORE_SCREENING;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScreeningDatePolicyTest {

    ScreeningDatePolicy screeningDatePolicy = new ScreeningDatePolicy();

    @ParameterizedTest
    @ValueSource(ints = {MIN_DAYS_BEFORE_SCREENING - 1, MAX_DAYS_BEFORE_SCREENING + 1})
    void screening_date_cannot_be_out_of_the_range(int daysNumber) {
        var screeningDate = SCREENING_DATE;
        var clock = createClock(screeningDate.minusDays(daysNumber));

        var exception = catchException(() -> screeningDatePolicy.validate(screeningDate, clock));

        assertEquals(ScreeningDateOutOfRangeException.class, exception.getClass());
    }

    private static Clock createClock(LocalDateTime date) {
        var instant = date.toInstant(ZONE_OFFSET);
        return Clock.fixed(instant, ZONE_OFFSET);
    }
}
