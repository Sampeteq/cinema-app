package com.cinema.tickets.domain.exceptions;

public class TicketAlreadyExistsException extends RuntimeException {

    public TicketAlreadyExistsException() {
        super("Ticket already exists");
    }
}
