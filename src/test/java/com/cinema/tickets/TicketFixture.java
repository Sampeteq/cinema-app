package com.cinema.tickets;

import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.users.domain.User;

public final class TicketFixture {

    private TicketFixture() {
    }

    public static Ticket createTicket(ScreeningSeat seat, User user) {
        return new Ticket(
                TicketStatus.BOOKED,
                seat,
                user
        );
    }

    public static Ticket createCancelledTicket(ScreeningSeat seat, User user) {
        return new Ticket(
                TicketStatus.CANCELLED,
                seat,
                user
        );
    }
}
