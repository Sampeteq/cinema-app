package com.cinema.tickets;

import com.cinema.halls.Seat;

class TicketFixtures {

    static final Seat SEAT = new Seat(1, 1);

    static Ticket createTicket(Long screeningId) {
        return new Ticket(screeningId, SEAT);
    }

    static Ticket createTicket(Long screeningId, Long userId) {
        return new Ticket(screeningId, SEAT, userId);
    }
}
