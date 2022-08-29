package com.example.ticket.domain.exception;

public class TooLateToCancelTicketReservationException extends RuntimeException {

    public TooLateToCancelTicketReservationException() {
        super("Too late to cancel ticket reservation.");
    }
}
