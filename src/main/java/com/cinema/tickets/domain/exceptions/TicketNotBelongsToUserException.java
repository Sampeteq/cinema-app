package com.cinema.tickets.domain.exceptions;

public class TicketNotBelongsToUserException extends RuntimeException {

    public TicketNotBelongsToUserException() {
        super("Ticket not belongs to a user");
    }
}
