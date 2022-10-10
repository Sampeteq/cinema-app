package com.example.ticket.exception;

public class TooLateToCancelTicketReservationException extends TicketException {

    public TooLateToCancelTicketReservationException() {
        super("Too late to cancel ticket reservation.");
    }
}
