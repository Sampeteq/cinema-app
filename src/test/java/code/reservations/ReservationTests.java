package code.reservations;

import code.SpringTestsSpec;
import code.films.FilmFacade;
import code.films.SampleFilms;
import code.films.dto.FilmDTO;
import code.reservations.dto.ReserveScreeningTicketDTO;
import code.reservations.dto.TicketDTO;
import code.reservations.exception.ReservationAlreadyCancelled;
import code.reservations.exception.TooLateToCancelReservationException;
import code.reservations.exception.TooLateToReservationException;
import code.screenings.ScreeningFacade;
import code.screenings.dto.ScreeningDTO;
import code.screenings.dto.ScreeningRoomDTO;
import code.screenings.exception.ScreeningNoFreeSeatsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;

import static code.screenings.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationTests extends SpringTestsSpec implements SampleFilms {

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    @Autowired
    private ReservationFacade reservationFacade;

    private List<FilmDTO> sampleFilms;

    private List<ScreeningDTO> sampleScreenings;

    private List<ScreeningRoomDTO> sampleRooms;

    private final Clock clock = Clock.systemUTC();


    @BeforeEach
    void initTestData() {
        sampleFilms = SampleFilms.addSampleFilms(filmFacade);
        sampleRooms = addSampleScreeningRooms(screeningFacade);
        sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
    }

    @Test
    void should_reserved_ticket() {
        var bookedTicket = reservationFacade.reserveTicket(
                sampleReserveTicketDTO(sampleScreenings.get(0).id()), clock
        );
        assertThat(
                reservationFacade.readTicket(bookedTicket.ticketUuid())
        ).isEqualTo(bookedTicket);
    }

    @Test
    void should_throw_exception_when_there_is_too_late_to_reserved_ticket() {
        var current = sampleScreenings
                .get(0)
                .date()
                .minusHours(23)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(current, ZoneOffset.UTC);
        assertThrows(
                TooLateToReservationException.class,
                () -> reservationFacade.reserveTicket(
                        sampleReserveTicketDTO(sampleScreenings.get(0).id()), clock
                )
        );
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() {
        var sampleScreeningWithNoFreeSeats = addSampleScreeningWithNoFreeSeats(
                sampleFilms.get(0).id(),
                screeningFacade
        );

        assertThrows(
                ScreeningNoFreeSeatsException.class,
                () -> reservationFacade.reserveTicket(
                        sampleReserveTicketDTO(sampleScreeningWithNoFreeSeats.id()),
                        clock
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_reservation() {
        var freeSeatsBeforeBooking = sampleScreenings.get(0).freeSeats();
        reservationFacade.reserveTicket(
                sampleReserveTicketDTO(sampleScreenings.get(0).id()),
                clock
        );
        assertThat(
                screeningFacade
                        .read(sampleScreenings.get(0).id())
                        .freeSeats()
        ).isEqualTo(freeSeatsBeforeBooking - 1);
    }

    @Test
    void should_increase_screening_free_seats_by_one_after_ticket_reservation_cancelling() {
        var sampleTicket = reserveSampleTicket(sampleScreenings.get(0).id());
        var freeSeatsAfterReservation = screeningFacade
                .read(sampleScreenings.get(0).id())
                .freeSeats();
        reservationFacade.cancelTicket(
                sampleTicket.ticketUuid(),
                clock
        );
        assertThat(
                screeningFacade
                        .read(sampleScreenings.get(0).id())
                        .freeSeats()
        ).isEqualTo(freeSeatsAfterReservation + 1);
    }

    @Test
    void should_cancel_ticket() {
        var sampleTicket = reserveSampleTicket(sampleScreenings.get(0).id());
        reservationFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThat(
                reservationFacade
                        .readTicket(sampleTicket.ticketUuid())
                        .status()
        ).isEqualTo(ScreeningTicketStatus.CANCELLED);
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() {
        var sampleTicket = reserveSampleTicket(sampleScreenings.get(0).id());
        reservationFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThrows(
                ReservationAlreadyCancelled.class,
                () -> reservationFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC())
        );
    }

    @Test
    void should_throw_exception_when_trying_cancel_ticket_when_there_is_less_than_24h_to_screening() {
        var sampleTicket = reservationFacade.reserveTicket(
                sampleReserveTicketDTO(sampleScreenings.get(0).id()),
                clock
        );
        var lessThanOneDayBeforeScreening = sampleScreenings.get(0)
                .date()
                .minusHours(23)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(lessThanOneDayBeforeScreening, ZoneOffset.UTC);
        assertThrows(
                TooLateToCancelReservationException.class,
                () -> reservationFacade.cancelTicket(sampleTicket.ticketUuid(), clock)
        );
    }

    @Test
    void should_return_all_tickets() {
        var sampleTickets = reserveSampleTickets(sampleScreenings.get(0).id());
        assertThat(
                reservationFacade.readAllTickets()
        ).isEqualTo(sampleTickets);
    }

    private static ReserveScreeningTicketDTO sampleReserveTicketDTO(Long sampleScreeningId) {
        return ReserveScreeningTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .build();
    }

    private TicketDTO reserveSampleTicket(Long sampleScreeningId) {
        return reservationFacade.reserveTicket(
                ReserveScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name")
                        .lastName("Lastname")
                        .build(),
                clock
        );
    }

    private List<TicketDTO> reserveSampleTickets(Long sampleScreeningId) {
        var sampleTicket1 = reservationFacade.reserveTicket(
                ReserveScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .build(),
                clock
        );
        var sampleTicket2 = reservationFacade.reserveTicket(
                ReserveScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 2")
                        .lastName("lastname 2")
                        .build(),
                clock
        );
        return List.of(sampleTicket1, sampleTicket2);
    }
}
