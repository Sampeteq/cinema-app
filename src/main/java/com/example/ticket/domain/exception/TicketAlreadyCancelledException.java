package com.example.ticket.domain.exception;

public class TicketAlreadyCancelledException extends TicketException {

    public TicketAlreadyCancelledException(Long ticketId) {
        super("Ticket already cancelled: " + ticketId);
    }
}
