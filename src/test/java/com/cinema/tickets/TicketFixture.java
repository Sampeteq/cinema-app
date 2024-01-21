package com.cinema.tickets;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;
import com.cinema.users.domain.User;

public final class TicketFixture {

    private TicketFixture() {
    }

    public static Ticket createTicket(Screening screening, Seat seat, User user) {
        return new Ticket(Ticket.Status.BOOKED, screening, seat, user);
    }

    public static Ticket createCancelledTicket(Screening screening, Seat seat, User user) {
        return new Ticket(
                Ticket.Status.CANCELLED,
                screening,
                seat,
                user
        );
    }
}
