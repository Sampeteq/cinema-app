package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;

public class TicketBookingPolicy {

    public void checkIfBookingIsPossible(long hoursLeftBeforeScreeningStart) {
        if (hoursLeftBeforeScreeningStart < 1) {
            throw new TicketBookTooLateException();
        }
    }
}
