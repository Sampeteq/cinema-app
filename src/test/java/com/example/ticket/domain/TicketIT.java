package com.example.ticket.domain;

import com.example.film.domain.FilmAPI;
import com.example.film.domain.FilmCategory;
import com.example.film.domain.FilmTestSpec;
import com.example.film.domain.dto.AddFilmDTO;
import com.example.film.domain.dto.FilmDTO;
import com.example.screening.domain.ScreeningAPI;
import com.example.screening.domain.ScreeningTestSpec;
import com.example.screening.domain.dto.AddScreeningDTO;
import com.example.screening.domain.dto.ScreeningDTO;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TooLateToCancelTicketReservationException;
import com.example.ticket.domain.exception.WrongTicketAgeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.ticket.domain.TicketValues.TICKET_BASIC_PRIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class TicketIT extends ScreeningTestSpec {

    @Autowired
    private TicketAPI ticketAPI;

    @Autowired
    private UnderageTicketDiscountPolicy underageTicketDiscountPolicy;

    @Test
    void should_reserve_ticket() {
       var sampleFilm= addSampleFilm();
       var sampleScreening= addSampleScreening(sampleFilm.filmId() );
       var reservedTicket= ticketAPI.reserveTicket(
               sampleReserveTicketDTO(sampleScreening.id(), 25)
       );
       assertThat(
               ticketAPI.readTicketById(reservedTicket.ticketId() )
       ).isEqualTo(reservedTicket);
    }

    @Test
    void should_apply_discount_for_children() {
        var sampleFilm = addSampleFilm();
        var sampleScreening= addSampleScreening(sampleFilm.filmId() );
        var sampleTicket= ticketAPI.reserveTicket(
                sampleReserveTicketDTO(sampleScreening.id(), 15 )
        );
        assertThat(
                ticketAPI.readTicketById(sampleTicket.ticketId() ).prize()
        ).isEqualTo(underageTicketDiscountPolicy.calculatePrize(TICKET_BASIC_PRIZE) );
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() {
        var sampleFilm = addSampleFilm();
        var sampleScreeningWithNoFreeSeats= addSampleScreeningWithNoFreeSeats(sampleFilm.filmId() );
        assertThrows(
                NoScreeningFreeSeatsException.class,
                () -> ticketAPI.reserveTicket(
                        sampleReserveTicketDTO(sampleScreeningWithNoFreeSeats.id(), 25 )
                )
        );
    }

    @Test
    void should_throw_exception_when_ticket_age_is_wrong() {
        var sampleFilm = addSampleFilm();
        var sampleScreening= addSampleScreening(sampleFilm.filmId() );
        assertThrows(
                WrongTicketAgeException.class,
                () -> ticketAPI.reserveTicket(
                        sampleReserveTicketDTO(sampleScreening.id(), sampleScreening.minAge() - 1 )
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_reservation() {
        var sampleFilm = addSampleFilm();
        var sampleScreening= addSampleScreening(sampleFilm.filmId() );
        var freeSeatsBeforeReservation= sampleScreening.freeSeats();
        ticketAPI.reserveTicket(
                sampleReserveTicketDTO(sampleScreening.id(), 25 )
        );
        assertThat(
                screeningAPI
                        .readScreeningById(sampleScreening.id() )
                        .freeSeats()
        ).isEqualTo(freeSeatsBeforeReservation - 1);
    }

    @Test
    void should_cancel_ticket_reservation() {
        var sampleFilm= addSampleFilm();
        var sampleScreening= addSampleScreening(sampleFilm.filmId() );
        var sampleTicket = reserveSampleTicket(sampleScreening.id() );
        var currentDate= sampleScreening.date().minusHours(48);
        ticketAPI.cancel(sampleTicket.ticketId(), currentDate);
        assertThat(
                ticketAPI
                        .readTicketById(sampleTicket.ticketId() )
                        .status()
        ).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void should_be_possible_to_cancel_ticket_reservation_at_least_one_day_before_screening() {
        var sampleFilm= addSampleFilm();
        var screeningDate= LocalDateTime.parse("2022-05-05T16:30");
        var sampleScreening= addSampleScreening(sampleFilm.filmId(), screeningDate);
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
       var sampleFilm= addSampleFilm();
       var sampleScreening= addSampleScreening(sampleFilm.filmId() );
       var sampleTickets= reserveSampleTickets(sampleScreening.id() );
       assertThat(
               ticketAPI.readAllTickets()
       ).isEqualTo(sampleTickets);
    }

    private static ReserveTicketDTO sampleReserveTicketDTO(UUID sampleScreeningId, int age) {
        return ReserveTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .age(age)
                .build();
    }

    private TicketDTO reserveSampleTicket(UUID sampleScreeningId) {
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

    private List<TicketDTO> reserveSampleTickets(UUID sampleScreeningId) {
        var sampleTicket1= ticketAPI.reserveTicket(
                ReserveTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .age(20)
                        .build()
        );
        var sampleTicket2= ticketAPI.reserveTicket(
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
