package com.example.ticket.domain.exception;

public class TicketNotFoundException extends TicketException {

    public TicketNotFoundException(Long id) {
        super("Ticket not found: " + id);
    }
}
