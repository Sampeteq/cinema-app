package com.cinema.tickets;

import com.cinema.halls.domain.Seat;
import com.cinema.tickets.domain.Ticket;

public class TicketFixtures {

    public static final Seat SEAT = new Seat(1, 1);

    public static Ticket createTicket(Long screeningId) {
        return new Ticket(screeningId, SEAT);
    }

    public static Ticket createTicket(Long screeningId, Long userId) {
        var ticket = new Ticket(screeningId, SEAT);
        ticket.assignUserId(userId);
        return ticket;
    }
}
