package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketCancellingPolicy {

    public void checkScreeningDate(long timeToScreeningInHours) {
        if (timeToScreeningInHours < 24) {
            throw new TicketCancelTooLateException();
        }
    }
}
