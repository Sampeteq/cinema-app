package com.cinema.tickets;

import com.cinema.tickets.exceptions.TicketCancelTooLateException;
import org.springframework.stereotype.Component;

@Component
class TicketCancellingPolicy {

    public void checkIfCancellingIsPossible(long hoursLeftBeforeScreeningStart) {
        if (hoursLeftBeforeScreeningStart < 24) {
            throw new TicketCancelTooLateException();
        }
    }
}
