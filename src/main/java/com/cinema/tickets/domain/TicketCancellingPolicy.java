package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;

public class TicketCancellingPolicy {

    public void checkIfCancellingIsPossible(long hoursLeftBeforeScreeningStart) {
        if (hoursLeftBeforeScreeningStart < 24) {
            throw new TicketCancelTooLateException();
        }
    }
}
