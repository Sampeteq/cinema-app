package com.cinema.tickets.domain;

import com.cinema.MockTimeProvider;
import com.cinema.tickets.TicketTestHelper;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.cinema.tickets.TicketTestHelper.prepareTicket;
import static com.cinema.tickets.TicketTestHelper.prepareCancelledTicket;
import static com.cinema.tickets.TicketTestHelper.prepareScreening;
import static com.cinema.tickets.TicketTestHelper.prepareScreeningWithBookedSeat;
import static com.cinema.tickets.TicketTestHelper.prepareSeat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketTests {

    private static final LocalDateTime currentDate = new MockTimeProvider().getCurrentDate();

    @Test
    void ticket_is_booked() {
        //given
        var seat = prepareSeat();
        var screening = prepareScreening(seat);
        var userId = 1L;

        //when
        var ticket = Ticket.book(currentDate, screening, seat.getRowNumber(), seat.getNumber(), userId);

        //then
        assertThat(ticket.getSeat()).isEqualTo(seat);
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.ACTIVE);
        assertThat(ticket.getUserId()).isEqualTo(userId);
        assertThat(ticket.getSeat().isFree()).isFalse();
    }

    @Test
    void ticket_is_unique() {
        //given
        var seat = prepareSeat();
        var screening = prepareScreeningWithBookedSeat(seat);
        var otherUserId = 2L;

        //when
        assertThrows(
                TicketAlreadyExists.class,
                () -> Ticket.book(
                        currentDate,
                        screening,
                        seat.getRowNumber(),
                        seat.getNumber(),
                        otherUserId
                )
        );
    }

    @Test
    void ticket_is_booked_at_least_1_hour_before_screening() {
        //given
        var seat = prepareSeat();
        var screeningDate = currentDate.minusMinutes(59);
        var screening = prepareScreening(seat, screeningDate);
        var userId = 1L;

        //when
        assertThrows(
                TicketBookTooLateException.class,
                () -> Ticket.book(
                        currentDate,
                        screening,
                        seat.getRowNumber(),
                        seat.getNumber(),
                        userId
                )
        );
    }

    @Test
    void ticket_is_cancelled() {
        //given
        var ticket = prepareTicket();

        //when
        ticket.cancel(currentDate);

        //then
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.CANCELLED);
        assertThat(ticket.getSeat().isFree()).isTrue();
        assertThat(ticket.getSeat().getTicket()).isNull();
    }

    @Test
    void ticket_is_cancelled_at_least_24h_hours_before_screening() {
        //given
        var screeningDate = currentDate.minusHours(23);
        var ticket = TicketTestHelper.prepareTicket(screeningDate);

        //when
        assertThrows(
                TicketCancelTooLateException.class,
                () -> ticket.cancel(currentDate)
        );
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() {
        //given
        var ticket = prepareCancelledTicket();

        //when
        assertThrows(
                TicketAlreadyCancelledException.class,
                () -> ticket.cancel(currentDate)
        );
    }
}
