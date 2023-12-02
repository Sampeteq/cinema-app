package com.cinema.tickets;

import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class TicketFixture {
    public static final Long SCREENING_ID = 1L;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);
    public static final int SEAT_ROW_NUMBER = 1;
    public static final int SEAT_NUMBER = 1;
    public static final long USER_ID = 1L;

    private TicketFixture() {
    }

    public static Ticket createTicket(Seat seat) {
        return new Ticket(
                TicketStatus.BOOKED,
                seat,
                USER_ID
        );
    }

    public static Ticket createTicket(Long userId, Seat seat) {
        return new Ticket(
                TicketStatus.BOOKED,
                seat,
                userId
        );
    }

    public static Ticket createCancelledTicket(Seat seat) {
        var ticket = createTicket(seat);
        ticket.cancel();
        return ticket;
    }

    public static Seat createSeat() {
        return new Seat(SEAT_ROW_NUMBER, SEAT_NUMBER, SCREENING_ID);
    }
}
