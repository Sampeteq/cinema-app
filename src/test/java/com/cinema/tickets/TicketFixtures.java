package com.cinema.tickets;

import com.cinema.halls.Seat;
import com.cinema.screenings.Screening;

class TicketFixtures {

    static final Seat SEAT = new Seat(1, 1);

    static Ticket createTicket(Screening screening) {
        return new Ticket(screening, SEAT);
    }

    static Ticket createTicket(Screening screening, Long userId) {
        return new Ticket(screening, SEAT, userId);
    }
}
