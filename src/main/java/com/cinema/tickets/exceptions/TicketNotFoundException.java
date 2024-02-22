package com.cinema.tickets.exceptions;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException() {
        super("Ticket not found");
    }
}
