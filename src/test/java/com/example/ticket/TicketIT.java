package com.example.ticket;

import com.example.screening.ScreeningTestSpec;
import com.example.screening.exception.NoScreeningFreeSeatsException;
import com.example.ticket.dto.BookTicketDTO;
import com.example.ticket.dto.TicketDTO;
import com.example.ticket.exception.TicketAlreadyCancelledException;
import com.example.ticket.exception.TooLateToCancelTicketException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketIT extends ScreeningTestSpec {

    @Autowired
    private TicketFacade ticketFacade;

    @Test
    void should_book_ticket() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var bookedTicket = ticketFacade.book(
                sampleBookTicketDTO(sampleScreening.id())
        );
        assertThat(
                ticketFacade.read(bookedTicket.ticketUuid())
        ).isEqualTo(bookedTicket);
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() {
        var sampleFilm = addSampleFilm();
        var sampleScreeningWithNoFreeSeats = addSampleScreeningWithNoFreeSeats(sampleFilm.id());
        assertThrows(
                NoScreeningFreeSeatsException.class,
                () -> ticketFacade.book(
                        sampleBookTicketDTO(sampleScreeningWithNoFreeSeats.id())
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_booking() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var freeSeatsBeforeBooking = sampleScreening.freeSeats();
        ticketFacade.book(
                sampleBookTicketDTO(sampleScreening.id())
        );
        assertThat(
                screeningFacade
                        .readScreening(sampleScreening.id())
                        .freeSeats()
        ).isEqualTo(freeSeatsBeforeBooking - 1);
    }

    @Test
    void should_cancel_ticket() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTicket = bookSampleTicket(sampleScreening.id());
        var twoDaysBeforeScreening = sampleScreening
                .date()
                .minusHours(48)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(twoDaysBeforeScreening, ZoneOffset.UTC);
        ticketFacade.cancel(sampleTicket.ticketUuid(), clock);
        assertThat(
                ticketFacade
                        .read(sampleTicket.ticketUuid())
                        .status()
        ).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTicket = bookSampleTicket(sampleScreening.id());
        var instant = sampleScreening
                .date()
                .minusDays(2)
                .toInstant(ZoneOffset.UTC);
        ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.fixed(instant, ZoneOffset.UTC));
        assertThrows(
                TicketAlreadyCancelledException.class,
                () -> ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.systemUTC())
        );
    }

    @Test
    void should_be_possible_to_cancel_ticket_at_least_one_day_before_screening() {
        var sampleFilm = addSampleFilm();
        var screeningDate = LocalDateTime.parse("2022-05-05T16:30");
        var sampleScreening = addSampleScreening(sampleFilm.id(), screeningDate);
        var sampleTicket = ticketFacade.book(
                sampleBookTicketDTO(sampleScreening.id())
        );
        var lessThanOneDayBeforeScreening = screeningDate
                .minusHours(15)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(lessThanOneDayBeforeScreening, ZoneOffset.UTC);
        assertThrows(
                TooLateToCancelTicketException.class,
                () -> ticketFacade.cancel(sampleTicket.ticketUuid(), clock)
        );
    }

    @Test
    void should_return_all_tickets() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTickets = bookSampleTickets(sampleScreening.id());
        assertThat(
                ticketFacade.readAll()
        ).isEqualTo(sampleTickets);
    }

    private static BookTicketDTO sampleBookTicketDTO(Long sampleScreeningId) {
        return BookTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .build();
    }

    private TicketDTO bookSampleTicket(Long sampleScreeningId) {
        return ticketFacade.book(
                BookTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name")
                        .lastName("Lastname")
                        .build()
        );
    }

    private List<TicketDTO> bookSampleTickets(Long sampleScreeningId) {
        var sampleTicket1 = ticketFacade.book(
                BookTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .build()
        );
        var sampleTicket2 = ticketFacade.book(
                BookTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 2")
                        .lastName("lastname 2")
                        .build()
        );
        return List.of(sampleTicket1, sampleTicket2);
    }
}
