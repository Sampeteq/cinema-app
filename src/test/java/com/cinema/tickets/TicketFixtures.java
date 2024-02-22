package com.cinema.tickets;

import com.cinema.halls.Seat;
import com.cinema.screenings.Screening;

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
