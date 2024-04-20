package com.cinema.screenings.domain;

import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.cinema.screenings.domain.ScreeningConstants.MAX_DAYS_BEFORE_SCREENING;
import static com.cinema.screenings.domain.ScreeningConstants.MIN_DAYS_BEFORE_SCREENING;

public class ScreeningDatePolicy {

    public void validate(LocalDateTime screeningDate, Clock clock) {
        var daysDifference = Duration
                .between(LocalDateTime.now(clock), screeningDate)
                .abs()
                .toDays();
        if (daysDifference < MIN_DAYS_BEFORE_SCREENING || daysDifference > MAX_DAYS_BEFORE_SCREENING) {
            throw new ScreeningDateOutOfRangeException();
        }
    }
}
