package com.cinema.tickets.domain.exceptions;

public class TicketNotBelongsToUser extends RuntimeException {

    public TicketNotBelongsToUser() {
        super("Ticket not belongs to a user");
    }
}
