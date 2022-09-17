package com.example.ticket.domain.exception;

import java.util.UUID;

public class TicketNotFoundException extends TicketException {

    public TicketNotFoundException(UUID id) {
        super("Ticket not found: " + id);
    }
}
