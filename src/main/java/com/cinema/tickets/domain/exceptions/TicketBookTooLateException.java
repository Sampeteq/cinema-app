package com.cinema.tickets.domain.exceptions;

public class TicketBookTooLateException extends RuntimeException {

    public TicketBookTooLateException() {
        super("Ticket can be booked at least 1h before screening");
    }
}
