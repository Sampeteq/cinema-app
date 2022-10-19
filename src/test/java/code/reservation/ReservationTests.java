package code.reservation;

import code.SpringTestsSpec;
import code.film.FilmFacade;
import code.film.dto.FilmDTO;
import code.reservation.dto.ReserveScreeningTicketDTO;
import code.reservation.dto.TicketDTO;
import code.screening.ScreeningFacade;
import code.screening.ScreeningTicketStatus;
import code.screening.dto.*;
import code.screening.exception.ScreeningFreeSeatsException;
import code.reservation.exception.ScreeningTicketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static code.film.FilmTestUtils.addSampleFilms;
import static code.screening.ScreeningTestUtils.*;
import static code.screening.ScreeningTestUtils.addSampleScreeningWithNoFreeSeats;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationTests extends SpringTestsSpec {

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    private List<FilmDTO> sampleFilms;

    private List<ScreeningDTO> sampleScreenings;

    private List<ScreeningRoomDTO> sampleRooms;

    private final Clock clock = Clock.systemUTC();


    @BeforeEach
    void initTestData() {
        sampleFilms = addSampleFilms(filmFacade);
        sampleRooms = addSampleScreeningRooms(screeningFacade);
        sampleScreenings = addSampleScreenings(sampleFilms, sampleRooms, screeningFacade);
    }

    @Test
    void should_reserved_ticket() {
        var bookedTicket = screeningFacade.reserveTicket(
                sampleReserveTicketDTO(sampleScreenings.get(0).id()), clock
        );
        assertThat(
                screeningFacade.readTicket(bookedTicket.ticketUuid())
        ).isEqualTo(bookedTicket);
    }

    @Test
    void should_throw_exception_when_there_is_too_late_to_reserved_ticket() {
        var currentDate = LocalDateTime.now(clock);
        var sampleScreening = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .date(currentDate.minusHours(23))
                        .minAge(13)
                        .roomUuid(sampleRooms.get(0).uuid())
                        .filmId(sampleFilms.get(0).id())
                        .build(),
                currentYear
        );
        assertThrows(
                ScreeningTicketException.class,
                () -> screeningFacade.reserveTicket(sampleReserveTicketDTO(sampleScreening.id()), clock)
        );
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() {
        var sampleScreeningWithNoFreeSeats = addSampleScreeningWithNoFreeSeats(
                sampleFilms.get(0).id(),
                screeningFacade
        );

        assertThrows(
                ScreeningFreeSeatsException.class,
                () -> screeningFacade.reserveTicket(
                        sampleReserveTicketDTO(sampleScreeningWithNoFreeSeats.id()),
                        clock
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_reservation() {
        var freeSeatsBeforeBooking = sampleRooms.get(0).freeSeats();
        screeningFacade.reserveTicket(
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
    void should_cancel_ticket() {
        var sampleTicket = reserveSampleTicket(sampleScreenings.get(0).id());
        screeningFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThat(
                screeningFacade
                        .readTicket(sampleTicket.ticketUuid())
                        .status()
        ).isEqualTo(ScreeningTicketStatus.CANCELLED);
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() {
        var sampleTicket = reserveSampleTicket(sampleScreenings.get(0).id());
        screeningFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThrows(
                ScreeningTicketException.class,
                () -> screeningFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC())
        );
    }

    @Test
    void should_throw_exception_when_trying_cancel_ticket_when_there_is_less_than_24h_to_screening() {
        var sampleTicket = screeningFacade.reserveTicket(
                sampleReserveTicketDTO(sampleScreenings.get(0).id()),
                clock
        );
        var lessThanOneDayBeforeScreening = sampleScreenings.get(0)
                .date()
                .minusHours(23)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(lessThanOneDayBeforeScreening, ZoneOffset.UTC);
        assertThrows(
                ScreeningTicketException.class,
                () -> screeningFacade.cancelTicket(sampleTicket.ticketUuid(), clock)
        );
    }

    @Test
    void should_return_all_tickets() {
        var sampleTickets = reserveSampleTickets(sampleScreenings.get(0).id());
        assertThat(
                screeningFacade.readAllTickets()
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
        return screeningFacade.reserveTicket(
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
        var sampleTicket1 = screeningFacade.reserveTicket(
                ReserveScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .build(),
                clock
        );
        var sampleTicket2 = screeningFacade.reserveTicket(
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
