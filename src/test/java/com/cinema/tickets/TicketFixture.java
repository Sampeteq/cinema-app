package com.cinema.tickets;

import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;

public final class TicketFixture {

    private TicketFixture() {
    }

    public static Ticket createTicket(Screening screening) {
        return new Ticket(screening, screening.getHall().getSeats().getFirst());
    }

    public static Ticket createTicket(Screening screening, Long userId) {
        return new Ticket(screening, screening.getHall().getSeats().getFirst(), userId);
    }
}
