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
}
