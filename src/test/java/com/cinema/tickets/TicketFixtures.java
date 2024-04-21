package com.cinema.tickets;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;
import com.cinema.users.domain.User;

import java.util.UUID;

import static com.cinema.screenings.ScreeningFixtures.createScreening;

public class TicketFixtures {

    public static final Seat SEAT = new Seat(1, 1);

    public static Ticket createTicket() {
        return new Ticket(UUID.randomUUID(), createScreening(), SEAT, null, 0);
    }

    public static Ticket createBookedTicket() {
        return new Ticket(UUID.randomUUID(), createScreening(), SEAT, UUID.randomUUID(), 0);
    }

    public static Ticket createTicket(Screening screening) {
        return new Ticket(UUID.randomUUID(), screening, SEAT, null, 0);
    }

    public static Ticket createTicket(Screening screening, User user) {
        return new Ticket(UUID.randomUUID(), screening, SEAT, user.getId(), 0);
    }

    public static Ticket createTicket(Screening screening, Seat seat) {
        return new Ticket(UUID.randomUUID(), screening, seat, null, 0);
    }

    public static Ticket createTicket(Screening screening, Seat seat, User user) {
        return new Ticket(UUID.randomUUID(), screening, seat, user.getId(), 0);
    }
}
