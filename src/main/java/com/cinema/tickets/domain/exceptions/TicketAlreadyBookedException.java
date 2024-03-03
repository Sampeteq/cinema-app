package com.cinema.tickets.domain.exceptions;

public class TicketAlreadyBookedException extends RuntimeException {

    public TicketAlreadyBookedException() {
        super("Ticket already booked");
    }
}
