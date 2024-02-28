package com.cinema.tickets;

import com.cinema.halls.Seat;

class TicketFixtures {

    static final Seat SEAT = new Seat(1, 1);

    static Ticket createTicket(Long screeningId) {
        return new Ticket(screeningId, SEAT);
    }

    static Ticket createTicket(Long screeningId, Long userId) {
        var ticket = new Ticket(screeningId, SEAT);
        ticket.assignUserId(userId);
        return ticket;
    }
}
