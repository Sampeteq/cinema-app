package code.ticket;

import code.SpringTestsSpec;
import code.film.FilmFacade;
import code.film.dto.FilmDTO;
import code.screening.ScreeningFacade;
import code.screening.dto.AddScreeningDTO;
import code.screening.dto.ScreeningDTO;
import code.screening.dto.ScreeningRoomDTO;
import code.screening.exception.NoScreeningFreeSeatsException;
import code.ticket.dto.BookTicketDTO;
import code.ticket.dto.TicketDTO;
import code.ticket.exception.TicketAlreadyCancelledException;
import code.ticket.exception.TooLateToBookTicketException;
import code.ticket.exception.TooLateToCancelTicketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static code.film.FilmTestUtils.addSampleFilms;
import static code.screening.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketIT extends SpringTestsSpec {

    @Autowired
    private TicketFacade ticketFacade;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    private final Clock clock = Clock.systemUTC();

    private List<FilmDTO> sampleFilms;

    private List<ScreeningDTO> sampleScreenings;

    private List<ScreeningRoomDTO> sampleRooms;

    @BeforeEach
    void initTestData() {
        sampleFilms = addSampleFilms(filmFacade);
        sampleRooms = addSampleScreeningRooms(screeningFacade);
        sampleScreenings = addSampleScreenings(sampleFilms, sampleRooms, screeningFacade);
    }

    @Test
    void should_book_ticket() {
        var bookedTicket = ticketFacade.book(
                sampleBookTicketDTO(sampleScreenings.get(0).id()), clock
        );
        assertThat(
                ticketFacade.read(bookedTicket.ticketUuid())
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
                TooLateToBookTicketException.class,
                () -> ticketFacade.book(sampleBookTicketDTO(sampleScreening.id()), clock)
        );
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() {
        var sampleScreeningWithNoFreeSeats = addSampleScreeningWithNoFreeSeats(
                sampleFilms.get(0).id(),
                screeningFacade
        );

        assertThrows(
                NoScreeningFreeSeatsException.class,
                () -> ticketFacade.book(
                        sampleBookTicketDTO(sampleScreeningWithNoFreeSeats.id()),
                        clock
                )
        );
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_booking() {
        var freeSeatsBeforeBooking = sampleRooms.get(0).freeSeats();
        ticketFacade.book(
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
        ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThat(
                ticketFacade
                        .read(sampleTicket.ticketUuid())
                        .status()
        ).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() {
        var sampleTicket = bookSampleTicket(sampleScreenings.get(0).id());
        ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThrows(
                TicketAlreadyCancelledException.class,
                () -> ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.systemUTC())
        );
    }

    @Test
    void should_throw_exception_when_trying_cancel_ticket_when_there_is_less_than_24h_to_screening() {
        var sampleTicket = ticketFacade.book(
                sampleBookTicketDTO(sampleScreenings.get(0).id()),
                clock
        );
        var lessThanOneDayBeforeScreening = sampleScreenings.get(0)
                .date()
                .minusHours(23)
                .toInstant(ZoneOffset.UTC);
        var clock = Clock.fixed(lessThanOneDayBeforeScreening, ZoneOffset.UTC);
        assertThrows(
                TooLateToCancelTicketException.class,
                () -> ticketFacade.cancel(sampleTicket.ticketUuid(), clock)
        );
    }

    @Test
    void should_return_all_tickets() {
        var sampleTickets = bookSampleTickets(sampleScreenings.get(0).id());
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
                        .build(),
                clock
        );
    }

    private List<TicketDTO> bookSampleTickets(Long sampleScreeningId) {
        var sampleTicket1 = ticketFacade.book(
                BookTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name 1")
                        .lastName("lastname 1")
                        .build(),
                clock
        );
        var sampleTicket2 = ticketFacade.book(
                BookTicketDTO
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
