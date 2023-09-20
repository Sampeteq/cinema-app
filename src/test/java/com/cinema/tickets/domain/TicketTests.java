package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import org.junit.jupiter.api.Test;

import static com.cinema.tickets.TicketTestHelper.CURRENT_DATE;
import static com.cinema.tickets.TicketTestHelper.FILM_TITLE;
import static com.cinema.tickets.TicketTestHelper.ROOM_CUSTOM_ID;
import static com.cinema.tickets.TicketTestHelper.ROW_NUMBER;
import static com.cinema.tickets.TicketTestHelper.SCREENING_DATE;
import static com.cinema.tickets.TicketTestHelper.SCREENING_ID;
import static com.cinema.tickets.TicketTestHelper.SEAT_NUMBER;
import static com.cinema.tickets.TicketTestHelper.USER_ID;
import static com.cinema.tickets.TicketTestHelper.prepareCancelledTicket;
import static com.cinema.tickets.TicketTestHelper.prepareTicket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketTests {

    @Test
    void ticket_is_booked() {
        //given
        var ticket = new Ticket(
                FILM_TITLE,
                SCREENING_ID,
                SCREENING_DATE,
                ROOM_CUSTOM_ID,
                ROW_NUMBER,
                SEAT_NUMBER
        );

        //when
        ticket.book(CURRENT_DATE, USER_ID);

        //then
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.ACTIVE);
        assertThat(ticket.getFilmTitle()).isEqualTo(FILM_TITLE);
        assertThat(ticket.getScreeningDate()).isEqualTo(SCREENING_DATE);
        assertThat(ticket.getRoomCustomId()).isEqualTo(ROOM_CUSTOM_ID);
        assertThat(ticket.getRowNumber()).isEqualTo(ROW_NUMBER);
        assertThat(ticket.getSeatNumber()).isEqualTo(SEAT_NUMBER);
        assertThat(ticket.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void ticket_is_booked_at_least_1_hour_before_screening() {
        //given
        var screeningDate = CURRENT_DATE.minusMinutes(59);
        var ticket = prepareTicket(screeningDate);

        //when
        assertThrows(
                TicketBookTooLateException.class,
                () -> ticket.book(CURRENT_DATE, USER_ID)
        );
    }

    @Test
    void ticket_is_cancelled() {
        //given
        var ticket = prepareTicket();
        ticket.book(CURRENT_DATE, USER_ID);

        //when
        ticket.cancel(CURRENT_DATE);

        //then
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void ticket_is_cancelled_at_least_24h_hours_before_screening() {
        //given
        var screeningDate = CURRENT_DATE.minusHours(23);
        var ticket = prepareTicket(screeningDate);

        //when
        assertThrows(
                TicketCancelTooLateException.class,
                () -> ticket.cancel(CURRENT_DATE)
        );
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() {
        //given
        var ticket = prepareCancelledTicket();

        //when
        assertThrows(
                TicketAlreadyCancelledException.class,
                () -> ticket.cancel(CURRENT_DATE)
        );
    }
}
