package com.example.ticket.domain.exception;

public class TooLateToCancelTicketReservationException extends TicketException {

    public TooLateToCancelTicketReservationException() {
        super("Too late to cancel ticket reservation.");
    }
}
