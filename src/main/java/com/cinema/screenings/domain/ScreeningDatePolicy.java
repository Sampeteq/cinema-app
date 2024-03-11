package com.cinema.screenings.domain;

import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ScreeningDatePolicy {

    private final Clock clock;

    void checkScreeningDate(LocalDateTime screeningDate) {
        var daysDifference = Duration
                .between(LocalDateTime.now(clock), screeningDate)
                .abs()
                .toDays();
        var isScreeningDateOutOfRange = daysDifference < 7 || daysDifference > 21;
        if (isScreeningDateOutOfRange) {
            throw new ScreeningDateOutOfRangeException();
        }
    }
}
