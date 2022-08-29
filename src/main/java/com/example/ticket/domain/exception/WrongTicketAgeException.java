package com.example.ticket.domain.exception;

public class WrongTicketAgeException extends RuntimeException {

    public WrongTicketAgeException(int ticketAge) {
        super("Wrong ticket age exception: " + ticketAge);
    }
}
