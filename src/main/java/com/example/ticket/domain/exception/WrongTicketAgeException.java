package com.example.ticket.domain.exception;

public class WrongTicketAgeException extends TicketException {

    public WrongTicketAgeException(int ticketAge) {
        super("Wrong ticket age exception: " + ticketAge);
    }
}
