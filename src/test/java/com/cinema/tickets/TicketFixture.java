package com.cinema.tickets;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;

public final class TicketFixture {
    public static final Long SCREENING_ID = 1L;
    public static final Long SEAT_ID = 1L;
    public static final long USER_ID = 1L;

    private TicketFixture() {
    }

    public static Ticket createTicket() {
        return new Ticket(
                TicketStatus.BOOKED,
                SCREENING_ID,
                SEAT_ID,
                USER_ID
        );
    }

    public static Ticket createCancelledTicket() {
        return new Ticket(
                TicketStatus.CANCELLED,
                SCREENING_ID,
                SEAT_ID,
                USER_ID
        );
    }
}
