package com.cinema.tickets;

import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;
import com.cinema.users.domain.User;

public final class TicketFixture {

    private TicketFixture() {
    }

    public static Ticket createTicket(Screening screening, User user) {
        return new Ticket(Ticket.Status.BOOKED, screening, screening.getHall().getSeats().getFirst(), user);
    }
}
