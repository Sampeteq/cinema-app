package com.cinema.tickets.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class TicketAlreadyCancelledException extends ValidationException {

    public TicketAlreadyCancelledException() {
        super("Ticket already cancelled");
    }
}
