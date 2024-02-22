package com.cinema.screenings;

import com.cinema.screenings.exceptions.ScreeningDateOutOfRangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
class ScreeningDatePolicy {

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
