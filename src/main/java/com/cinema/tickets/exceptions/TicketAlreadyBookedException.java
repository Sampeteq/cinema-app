package com.cinema.tickets.exceptions;

public class TicketAlreadyBookedException extends RuntimeException {

    public TicketAlreadyBookedException() {
        super("Ticket already booked");
    }
}
