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

    public void checkScreeningDate(long timeToScreeningInHours) {
        if (timeToScreeningInHours < 1) {
            throw new TicketBookTooLateException();
        }
    }
}
