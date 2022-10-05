package com.example.ticket.domain;

import com.example.screening.domain.ScreeningTestSpec;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TicketAlreadyCancelledException;
import com.example.ticket.domain.exception.TooLateToCancelTicketReservationException;
import com.example.ticket.domain.exception.WrongTicketAgeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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
    void should_reserve_ticket() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var reservedTicket = ticketFacade.reserve(
                sampleReserveTicketDTO(sampleScreening.id(), 25)
        );
        assertThat(
                ticketFacade.read(reservedTicket.ticketId())
        ).isEqualTo(reservedTicket);
    }

    @Test
    void should_apply_discount_for_children() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTicket = ticketFacade.reserve(
                sampleReserveTicketDTO(sampleScreening.id(), 15)
        );
        assertThat(
                ticketFacade
                        .read(sampleTicket.ticketId())
                        .prize()
        ).isEqualTo(
                new BigDecimal("9.00")
        );
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() {
        var sampleFilm = addSampleFilm();
        var sampleScreeningWithNoFreeSeats = addSampleScreeningWithNoFreeSeats(sampleFilm.id());
        assertThrows(
                NoScreeningFreeSeatsException.class,
                () -> ticketFacade.reserve(
                        sampleReserveTicketDTO(sampleScreeningWithNoFreeSeats.id(), 25)
                )
        );
    }

    @Test
    void should_throw_exception_when_ticket_age_is_wrong() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        assertThrows(
                WrongTicketAgeException.class,
                () -> ticketFacade.reserve(
                        sampleReserveTicketDTO(sampleScreening.id(), sampleScreening.minAge() - 1)
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_reservation() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var freeSeatsBeforeReservation = sampleScreening.freeSeats();
        ticketFacade.reserve(
                sampleReserveTicketDTO(sampleScreening.id(), 25)
        );
        assertThat(
                screeningFacade
                        .readScreening(sampleScreening.id())
                        .freeSeats()
        ).isEqualTo(freeSeatsBeforeReservation - 1);
    }

    @Test
    void should_cancel_ticket_reservation() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTicket = reserveSampleTicket(sampleScreening.id());
        var twoDaysBeforeScreening = sampleScreening
                .date()
                .minusHours(48)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(twoDaysBeforeScreening, ZoneOffset.UTC);
        ticketFacade.cancel(sampleTicket.ticketId(), clock);
        assertThat(
                ticketFacade
                        .read(sampleTicket.ticketId())
                        .status()
        ).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTicket = reserveSampleTicket(sampleScreening.id());
        var instant = sampleScreening
                .date()
                .minusDays(2)
                .toInstant(ZoneOffset.UTC);
        ticketFacade.cancel(sampleTicket.ticketId(), Clock.fixed(instant, ZoneOffset.UTC));
        assertThrows(
                TicketAlreadyCancelledException.class,
                () -> ticketFacade.cancel(sampleTicket.ticketId(), Clock.systemUTC())
        );
    }

    @Test
    void should_be_possible_to_cancel_ticket_reservation_at_least_one_day_before_screening() {
        var sampleFilm = addSampleFilm();
        var screeningDate = LocalDateTime.parse("2022-05-05T16:30");
        var sampleScreening = addSampleScreening(sampleFilm.id(), screeningDate);
        var sampleTicket = ticketFacade.reserve(
                sampleReserveTicketDTO(sampleScreening.id(), 25)
        );
        var lessThanOneDayBeforeScreening = screeningDate
                .minusHours(15)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(lessThanOneDayBeforeScreening, ZoneOffset.UTC);
        assertThrows(
                TooLateToCancelTicketReservationException.class,
                () -> ticketFacade.cancel(sampleTicket.ticketId(), clock)
        );
    }

    @Test
    void should_return_all_tickets() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTickets = reserveSampleTickets(sampleScreening.id());
        assertThat(
                ticketFacade.readAll()
        ).isEqualTo(sampleTickets);
    }

    private static ReserveTicketDTO sampleReserveTicketDTO(Long sampleScreeningId, int age) {
        return ReserveTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .age(age)
                .build();
    }

    private TicketDTO reserveSampleTicket(Long sampleScreeningId) {
        return ticketFacade.reserve(
                ReserveTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name")
                        .lastName("Lastname")
                        .age(50)
                        .build()
        );
    }

    private List<TicketDTO> reserveSampleTickets(Long sampleScreeningId) {
        var sampleTicket1 = ticketFacade.reserve(
                ReserveTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .age(20)
                        .build()
        );
        var sampleTicket2 = ticketFacade.reserve(
                ReserveTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 2")
                        .lastName("lastname 2")
                        .age(30)
                        .build()
        );
        return List.of(sampleTicket1, sampleTicket2);
    }
}
