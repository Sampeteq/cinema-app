package com.cinema.tickets.domain;

import com.cinema.ClockFixtures;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.cinema.tickets.TicketFixtures.createBookedTicket;
import static com.cinema.tickets.TicketFixtures.createTicket;
import static com.cinema.tickets.domain.TicketConstants.MIN_BOOKING_HOURS;
import static com.cinema.tickets.domain.TicketConstants.MIN_CANCELLATION_HOURS;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TicketTest {

    @Test
    void ticket_is_booked() {
        var ticket = createTicket();
        var clock = createClock(ticket.getScreening().getDate().plusHours(MIN_BOOKING_HOURS));
        var userId = UUID.randomUUID();

        ticket.book(clock, userId);

        assertEquals(userId, ticket.getUserId());
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() {
        var ticket = createTicket();
        var clock = createClock(ticket.getScreening().getDate().plusHours(MIN_BOOKING_HOURS - 1));
        var userId = UUID.randomUUID();

        var exception = catchException(() -> ticket.book(clock, userId));

        assertEquals(TicketBookTooLateException.class, exception.getClass());
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        var ticket = createBookedTicket();
        var clock = createClock(ticket.getScreening().getDate().plusHours(MIN_BOOKING_HOURS));
        var userId = UUID.randomUUID();

        var exception = catchException(() -> ticket.book(clock, userId));

        assertEquals(TicketAlreadyBookedException.class, exception.getClass());
    }

    @Test
    void ticket_is_cancelled() {
        var ticket = createBookedTicket();
        var clock = createClock(ticket.getScreening().getDate().plusHours(MIN_CANCELLATION_HOURS));

        ticket.cancel(clock);

        assertNull(ticket.getUserId());
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        var ticket = createBookedTicket();
        var clock = createClock(ticket.getScreening().getDate().plusHours(MIN_CANCELLATION_HOURS - 1));

        var exception = catchException(() -> ticket.cancel(clock));

        assertEquals(TicketCancelTooLateException.class, exception.getClass());
    }

    private static Clock createClock(LocalDateTime date) {
        var instant = date
                .plusHours(MIN_BOOKING_HOURS - 1)
                .toInstant(ClockFixtures.ZONE_OFFSET);
        return Clock.fixed(instant, ClockFixtures.ZONE_OFFSET);
    }
}
