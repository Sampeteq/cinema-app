package com.cinema.tickets.domain.exceptions;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException() {
        super("Ticket not found");
    }
}
