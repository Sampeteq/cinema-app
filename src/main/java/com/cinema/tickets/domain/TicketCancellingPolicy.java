package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TicketCancellingPolicy {

    private final Clock clock;

    public void checkScreeningDate(LocalDateTime screeningDate) {
        if (timeToScreeningInHours(clock, screeningDate) < 24) {
            throw new TicketCancelTooLateException();
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
