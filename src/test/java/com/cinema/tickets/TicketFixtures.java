package com.cinema.tickets;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;

public final class TicketFixtures {

    public static final Seat SEAT = new Seat(1, 1);

    private TicketFixtures() {
    }

    public static Ticket createTicket(Screening screening) {
        return new Ticket(screening, SEAT);
    }

    public static Ticket createTicket(Screening screening, Long userId) {
        return new Ticket(screening, SEAT, userId);
    }
}
