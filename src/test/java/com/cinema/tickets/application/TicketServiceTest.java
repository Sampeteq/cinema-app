package com.cinema.tickets.application;

import com.cinema.halls.application.HallService;
import com.cinema.halls.domain.Seat;
import com.cinema.screenings.application.ScreeningService;
import com.cinema.screenings.application.exceptions.ScreeningNotFoundException;
import com.cinema.tickets.domain.TicketReadRepository;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.application.exceptions.TicketNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cinema.screenings.ScreeningFixtures.createScreening;
import static com.cinema.tickets.TicketFixtures.createTicket;
import static com.cinema.users.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketServiceTest {

    TicketRepository ticketRepository = mock();
    TicketReadRepository ticketReadRepository = mock();
    ScreeningService screeningService = mock();
    HallService hallService = mock();
    Clock clock = mock();
    TicketService ticketService = new TicketService(
            ticketRepository,
            ticketReadRepository,
            screeningService,
            hallService,
            clock
    );

    @Test
    void ticket_is_booked() {
        var screening = createScreening();
        var seat = new Seat(1, 1);
        var user = createUser();
        var ticket = createTicket(screening, seat);
        when(screeningService.getScreeningById(screening.getId())).thenReturn(screening);
        when(ticketRepository.getByScreeningIdAndSeat(screening.getId(), seat)).thenReturn(Optional.of(ticket));
        setCurrentDate(screening.getDate().plusHours(1));

        ticketService.bookTickets(screening.getId(), List.of(seat), user);

        assertEquals(user.getId(), ticket.getUserId());
    }

    @Test
    void ticket_is_booked_for_existing_screening() {
        var screeningId = UUID.randomUUID();
        var seats = List.of(new Seat(1, 1));
        var user = createUser();
        when(screeningService.getScreeningById(screeningId)).thenThrow(ScreeningNotFoundException.class);

        var exception = catchException(() -> ticketService.bookTickets(screeningId, seats, user));

        assertEquals(ScreeningNotFoundException.class, exception.getClass());
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() {
        var screening = createScreening();
        var seats = List.of(new Seat(1, 1));
        var user = createUser();
        when(screeningService.getScreeningById(screening.getId())).thenReturn(screening);
        setCurrentDate(screening.getDate().plusMinutes(59));

        var exception = catchException(() -> ticketService.bookTickets(screening.getId(), seats, user));

        assertEquals(TicketBookTooLateException.class, exception.getClass());
    }

    @Test
    void ticket_is_cancelled() {
        var screening = createScreening();
        var seat = new Seat(1, 1);
        var user = createUser();
        var ticket = createTicket(screening, seat, user);
        when(ticketRepository.getByIdAndUserId(ticket.getId(), user.getId())).thenReturn(Optional.of(ticket));
        setCurrentDate(screening.getDate().plusHours(24));

        ticketService.cancelTicket(ticket.getId(), user);

        assertNull(ticket.getUserId());
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        var screening = createScreening();
        var seat = new Seat(1, 1);
        var user = createUser();
        var ticket = createTicket(screening, seat, user);
        when(ticketRepository.getByIdAndUserId(ticket.getId(), user.getId())).thenReturn(Optional.of(ticket));
        setCurrentDate(screening.getDate().plusHours(23));

        var exception = catchException(() -> ticketService.cancelTicket(ticket.getId(), user));

        assertEquals(TicketCancelTooLateException.class, exception.getClass());
    }

    @Test
    void ticket_is_booked_for_existing_seat() {
        var screening = createScreening();
        var seat = new Seat(1, 1);
        var user = createUser();
        when(screeningService.getScreeningById(screening.getId())).thenReturn(screening);
        when(ticketRepository.getByScreeningIdAndSeat(screening.getId(), seat)).thenReturn(Optional.empty());
        setCurrentDate(screening.getDate().plusHours(24));

        var exception = catchException(() -> ticketService.bookTickets(screening.getId(), List.of(seat), user));

        assertEquals(TicketNotFoundException.class, exception.getClass());
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        var screening = createScreening();
        var seat = new Seat(1, 1);
        var user = createUser();
        var ticket = createTicket(screening, seat, user);
        when(screeningService.getScreeningById(screening.getId())).thenReturn(screening);
        when(ticketRepository.getByScreeningIdAndSeat(screening.getId(), seat)).thenReturn(Optional.of(ticket));
        setCurrentDate(screening.getDate().plusHours(24));

        var exception = catchException(() -> ticketService.bookTickets(screening.getId(), List.of(seat), user));

        assertEquals(TicketAlreadyBookedException.class, exception.getClass());
    }

    private void setCurrentDate(LocalDateTime date) {
        when(clock.instant()).thenReturn(date.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }
}