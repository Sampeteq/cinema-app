package com.cinema.tickets.domain.exceptions;

public class TicketAlreadyExists extends RuntimeException {

    public TicketAlreadyExists() {
        super("Ticket already exists");
    }
}
