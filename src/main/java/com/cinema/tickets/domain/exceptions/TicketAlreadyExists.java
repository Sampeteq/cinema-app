package com.cinema.tickets.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class TicketAlreadyExists extends ValidationException {

    public TicketAlreadyExists() {
        super("Ticket already exists");
    }
}
