package com.cinema.tickets.application.exceptions;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException() {
        super("Ticket not found");
    }
}
