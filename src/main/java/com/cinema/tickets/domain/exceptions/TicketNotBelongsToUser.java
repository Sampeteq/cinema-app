package com.cinema.tickets.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class TicketNotBelongsToUser extends ValidationException {

    public TicketNotBelongsToUser() {
        super("Ticket not belongs to a user");
    }
}
