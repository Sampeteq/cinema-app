package com.cinema.tickets.domain.exceptions;

public class TicketCancelTooLateException extends RuntimeException {

    public TicketCancelTooLateException() {
        super("Ticket can be cancelled at least 24h before screening");
    }
}
