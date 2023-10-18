package com.cinema.tickets.domain.exceptions;

public class TicketCancelTooLateException extends RuntimeException {

    public TicketCancelTooLateException() {
        super("Too late to cancel ticket");
    }
}
