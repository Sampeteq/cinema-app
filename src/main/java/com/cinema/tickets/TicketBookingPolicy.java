package com.cinema.tickets;

import com.cinema.tickets.exceptions.TicketBookTooLateException;
import org.springframework.stereotype.Component;

@Component
class TicketBookingPolicy {

    public void checkIfBookingIsPossible(long hoursLeftBeforeScreeningStart) {
        if (hoursLeftBeforeScreeningStart < 1) {
            throw new TicketBookTooLateException();
        }
    }
}
