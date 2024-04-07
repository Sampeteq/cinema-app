package com.cinema.tickets;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;
import com.cinema.users.domain.User;

public class TicketFixtures {

    public static final Seat SEAT = new Seat(1, 1);

    public static Ticket createTicket(Screening screening) {
        return new Ticket(null, screening, SEAT, null, 0);
    }

    public static Ticket createTicket(Screening screening, User user) {
        var ticket = new Ticket(null, screening, SEAT, null, 0);
        ticket.assignUser(user.getId());
        return ticket;
    }

    public static Ticket createTicket(Screening screening, Seat seat) {
        return new Ticket(null, screening, seat, null, 0);
    }

    public static Ticket createTicket(Screening screening, Seat seat, User user) {
        return new Ticket(1L, screening, seat, user.getId(), 0);
    }
}
