package code.screening;

import code.SpringTestsSpec;
import code.film.FilmFacade;
import code.film.dto.FilmDTO;
import code.screening.dto.AddScreeningDTO;
import code.screening.dto.AddScreeningRoomDTO;
import code.screening.dto.ScreeningDTO;
import code.screening.dto.ScreeningRoomDTO;
import code.screening.exception.*;
import code.screening.dto.BookScreeningTicketDTO;
import code.screening.dto.TicketDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static code.film.FilmTestUtils.addSampleFilms;
import static code.screening.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreeningIT extends SpringTestsSpec {

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
    void should_add_screening() {
        var addedScreening = screeningFacade.add(
                sampleAddScreeningDTO(sampleFilms.get(0).id(), sampleRooms.get(0).uuid()),
                currentYear
        );
        assertThat(
                screeningFacade.read(addedScreening.id())
        ).isEqualTo(addedScreening);
    }

    @ParameterizedTest
    @MethodSource("code.screening.ScreeningTestUtils#getWrongScreeningYears")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(Integer wrongScreeningYear) {
        assertThrows(
                ScreeningYearException.class,
                () -> screeningFacade.add(
                        sampleAddScreeningDTOWithWrongScreeningYear(
                                sampleFilms.get(0).id(),
                                sampleRooms.get(0).uuid(),
                                wrongScreeningYear
                        ),
                        currentYear
                )
        );
    }

    @Test
    void should_return_all_screenings() {
        assertThat(
                screeningFacade.readAll()
        ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_return_screenings_by_film_id() {
        assertThat(
                screeningFacade.readByFilmId(sampleFilms.get(0).id())
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilms.get(0).id())
        );
    }

    @Test
    void should_return_screenings_by_date() {
        assertThat(
                screeningFacade.readByDate(sampleScreenings.get(0).date(), currentYear)
        ).allMatch(
                screening -> screening.date().equals(sampleScreenings.get(0).date())
        );
    }

    @Test
    void should_add_screening_room() {
        var addedScreeningRoom = screeningFacade.addRoom(
                AddScreeningRoomDTO
                        .builder()
                        .number(5)
                        .freeSeats(200)
                        .build()
        );
        assertThat(
                screeningFacade.readRoom(addedScreeningRoom.uuid())
        ).isEqualTo(addedScreeningRoom);
    }

    @Test
    void should_throw_exception_when_room_number_is_not_unique() {
        assertThrows(
                ScreeningRoomException.class,
                () -> screeningFacade.addRoom(
                        AddScreeningRoomDTO
                                .builder()
                                .number(sampleRooms.get(0).number())
                                .freeSeats(200)
                                .build()
                )
        );
    }

    @Test
    void should_book_ticket() {
        var bookedTicket = screeningFacade.bookTicket(
                sampleBookTicketDTO(sampleScreenings.get(0).id()), clock
        );
        assertThat(
                screeningFacade.readTicket(bookedTicket.ticketUuid())
        ).isEqualTo(bookedTicket);
    }

    @Test
    void should_throw_exception_when_there_is_too_late_to_book_ticket() {
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
                () -> screeningFacade.bookTicket(sampleBookTicketDTO(sampleScreening.id()), clock)
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
                () -> screeningFacade.bookTicket(
                        sampleBookTicketDTO(sampleScreeningWithNoFreeSeats.id()),
                        clock
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_booking() {
        var freeSeatsBeforeBooking = sampleRooms.get(0).freeSeats();
        screeningFacade.bookTicket(
                sampleBookTicketDTO(sampleScreenings.get(0).id()),
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
        var sampleTicket = bookSampleTicket(sampleScreenings.get(0).id());
        screeningFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThat(
                screeningFacade
                        .readTicket(sampleTicket.ticketUuid())
                        .status()
        ).isEqualTo(ScreeningTicketStatus.CANCELLED);
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() {
        var sampleTicket = bookSampleTicket(sampleScreenings.get(0).id());
        screeningFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThrows(
                ScreeningTicketException.class,
                () -> screeningFacade.cancelTicket(sampleTicket.ticketUuid(), Clock.systemUTC())
        );
    }

    @Test
    void should_throw_exception_when_trying_cancel_ticket_when_there_is_less_than_24h_to_screening() {
        var sampleTicket = screeningFacade.bookTicket(
                sampleBookTicketDTO(sampleScreenings.get(0).id()),
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
        var sampleTickets = bookSampleTickets(sampleScreenings.get(0).id());
        assertThat(
                screeningFacade.readAllTickets()
        ).isEqualTo(sampleTickets);
    }

    private static BookScreeningTicketDTO sampleBookTicketDTO(Long sampleScreeningId) {
        return BookScreeningTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .build();
    }

    private TicketDTO bookSampleTicket(Long sampleScreeningId) {
        return screeningFacade.bookTicket(
                BookScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name")
                        .lastName("Lastname")
                        .build(),
                clock
        );
    }

    private List<TicketDTO> bookSampleTickets(Long sampleScreeningId) {
        var sampleTicket1 = screeningFacade.bookTicket(
                BookScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .build(),
                clock
        );
        var sampleTicket2 = screeningFacade.bookTicket(
                BookScreeningTicketDTO
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

