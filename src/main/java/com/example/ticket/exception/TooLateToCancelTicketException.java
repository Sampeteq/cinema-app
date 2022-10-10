package com.example.ticket.exception;

public class TooLateToCancelTicketException extends TicketException {

    public TooLateToCancelTicketException() {
        super("Too late to cancel ticket reservation.");
    }
}
