package com.cinema.tickets.domain.exceptions;

public class TicketAlreadyCancelledException extends RuntimeException {

    public TicketAlreadyCancelledException() {
        super("Ticket already cancelled");
    }
}
