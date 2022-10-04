package com.example.ticket.domain;

import com.example.screening.domain.ScreeningId;
import com.example.screening.domain.ScreeningTestSpec;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TooLateToCancelTicketReservationException;
import com.example.ticket.domain.exception.WrongTicketAgeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.ticket.domain.TicketValues.TICKET_BASIC_PRIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketIT extends ScreeningTestSpec {

    @Autowired
    private TicketAPI ticketAPI;

    @Autowired
    private UnderageTicketDiscountPolicy underageTicketDiscountPolicy;

    @Test
    void should_reserve_ticket() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var reservedTicket = ticketAPI.reserveTicket(
                sampleReserveTicketDTO(sampleScreening.id(), 25)
        );
        assertThat(
                ticketAPI.readTicketById(reservedTicket.ticketId())
        ).isEqualTo(reservedTicket);
    }

    @Test
    void should_apply_discount_for_children() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTicket = ticketAPI.reserveTicket(
                sampleReserveTicketDTO(sampleScreening.id(), 15)
        );
        assertThat(
                ticketAPI
                        .readTicketById(sampleTicket.ticketId())
                        .prize()
        ).isEqualTo(
                underageTicketDiscountPolicy
                        .calculate(Money.of(TICKET_BASIC_PRIZE))
                        .getValue()
        );
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() {
        var sampleFilm = addSampleFilm();
        var sampleScreeningWithNoFreeSeats = addSampleScreeningWithNoFreeSeats(sampleFilm.id());
        assertThrows(
                NoScreeningFreeSeatsException.class,
                () -> ticketAPI.reserveTicket(
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
                () -> ticketAPI.reserveTicket(
                        sampleReserveTicketDTO(sampleScreening.id(), sampleScreening.minAge() - 1)
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_reservation() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var freeSeatsBeforeReservation = sampleScreening.freeSeats();
        ticketAPI.reserveTicket(
                sampleReserveTicketDTO(sampleScreening.id(), 25)
        );
        assertThat(
                screeningAPI
                        .readScreeningById(sampleScreening.id())
                        .freeSeats()
        ).isEqualTo(freeSeatsBeforeReservation - 1);
    }

    @Test
    void should_cancel_ticket_reservation() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTicket = reserveSampleTicket(sampleScreening.id());
        var currentDate = sampleScreening.date().minusHours(48);
        ticketAPI.cancel(sampleTicket.ticketId(), currentDate);
        assertThat(
                ticketAPI
                        .readTicketById(sampleTicket.ticketId())
                        .status()
        ).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void should_be_possible_to_cancel_ticket_reservation_at_least_one_day_before_screening() {
        var sampleFilm = addSampleFilm();
        var screeningDate = LocalDateTime.parse("2022-05-05T16:30");
        var sampleScreening = addSampleScreening(sampleFilm.id(), screeningDate);
        var sampleTicket = ticketAPI.reserveTicket(
                sampleReserveTicketDTO(sampleScreening.id(), 25)
        );
        var currentDate = screeningDate.minusHours(15);
        assertThrows(
                TooLateToCancelTicketReservationException.class,
                () -> ticketAPI.cancel(sampleTicket.ticketId(), currentDate)
        );
    }

    @Test
    void should_return_all_tickets() {
        var sampleFilm = addSampleFilm();
        var sampleScreening = addSampleScreening(sampleFilm.id());
        var sampleTickets = reserveSampleTickets(sampleScreening.id());
        assertThat(
                ticketAPI.readAllTickets()
        ).isEqualTo(sampleTickets);
    }

    private static ReserveTicketDTO sampleReserveTicketDTO(ScreeningId sampleScreeningId, int age) {
        return ReserveTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .age(age)
                .build();
    }

    private TicketDTO reserveSampleTicket(ScreeningId sampleScreeningId) {
        return ticketAPI.reserveTicket(
                ReserveTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name")
                        .lastName("Lastname")
                        .age(50)
                        .build()
        );
    }

    private List<TicketDTO> reserveSampleTickets(ScreeningId sampleScreeningId) {
        var sampleTicket1 = ticketAPI.reserveTicket(
                ReserveTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .age(20)
                        .build()
        );
        var sampleTicket2 = ticketAPI.reserveTicket(
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
