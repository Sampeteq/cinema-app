package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TicketBookingPolicy {

    private final Clock clock;

    public void checkScreeningDate(LocalDateTime screeningDate) {
        if (timeToScreeningInHours(clock, screeningDate) < 1) {
            throw new TicketBookTooLateException();
        }
    }

    private long timeToScreeningInHours(Clock clock, LocalDateTime screeningDate) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, screeningDate)
                .abs()
                .toHours();
    }
}
