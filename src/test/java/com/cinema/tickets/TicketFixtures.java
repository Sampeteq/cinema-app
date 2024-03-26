package com.cinema.tickets;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;
import com.cinema.users.domain.User;

public class TicketFixtures {

    public static final Seat SEAT = new Seat(1, 1);

    public static Ticket createTicket(Screening screening) {
        return new Ticket(screening, SEAT);
    }

    public static Ticket createTicket(Screening screening, User user) {
        var ticket = new Ticket(screening, SEAT);
        ticket.assignUser(user);
        return ticket;
    }

    public static Ticket createTicket(Screening screening, Seat seat) {
        return new Ticket(screening, seat);
    }

    public static Ticket createTicket(Screening screening, Seat seat, User user) {
        var ticket = new Ticket(screening, seat);
        ticket.assignUser(user);
        ticket.setId(1L);
        return ticket;
    }
}
