package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBookingPolicy {

    public void checkIfBookingIsPossible(long hoursLeftBeforeScreeningStart) {
        if (hoursLeftBeforeScreeningStart < 1) {
            throw new TicketBookTooLateException();
        }
    }
}
