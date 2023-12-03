package com.cinema.tickets;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class TicketFixture {
    public static final Long SCREENING_ID = 1L;
    public static final Long HALL_ID = 1L;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);
    public static final Long SEAT_ID = 1L;
    public static final int SEAT_ROW_NUMBER = 1;
    public static final int SEAT_NUMBER = 1;
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

    public static Ticket createTicket(Long userId) {
        return new Ticket(
                TicketStatus.BOOKED,
                SCREENING_ID,
                SEAT_ID,
                userId
        );
    }

    public static Ticket createCancelledTicket() {
        var ticket = createTicket();
        ticket.cancel();
        return ticket;
    }
}
