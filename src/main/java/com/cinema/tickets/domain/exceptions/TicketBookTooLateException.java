package com.cinema.tickets.domain.exceptions;

public class TicketBookTooLateException extends RuntimeException {

    public TicketBookTooLateException() {
        super("Too late for ticket booking");
    }
}
