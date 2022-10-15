package code.ticket;

import code.SpringTestsSpec;
import code.film.FilmFacade;
import code.screening.ScreeningFacade;
import code.screening.dto.AddScreeningDTO;
import code.screening.exception.NoScreeningFreeSeatsException;
import code.ticket.dto.BookTicketDTO;
import code.ticket.dto.TicketDTO;
import code.ticket.exception.TicketAlreadyCancelledException;
import code.ticket.exception.TooLateToBookTicketException;
import code.ticket.exception.TooLateToCancelTicketException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static code.film.FilmTestUtils.addFilm;
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

    @Test
    void should_book_ticket() {
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreening = addSampleScreening(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
        var bookedTicket = ticketFacade.book(
                sampleBookTicketDTO(sampleScreening.id()), clock
        );
        assertThat(
                ticketFacade.read(bookedTicket.ticketUuid())
        ).isEqualTo(bookedTicket);
    }

    @Test
    void should_throw_exception_when_there_is_too_late_to_book_ticket() {
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var currentDate = LocalDateTime.now(clock);
        var sampleScreening = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .date(currentDate.minusHours(23))
                        .minAge(13)
                        .roomUuid(sampleRoom.uuid())
                        .filmId(sampleFilm.id())
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
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoomWithNoFreeSeats(screeningFacade);
        var sampleScreeningWithNoFreeSeats = addSampleScreening(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
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
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreening = addSampleScreening(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
        var freeSeatsBeforeBooking = sampleRoom.freeSeats();
        ticketFacade.book(
                sampleBookTicketDTO(sampleScreening.id()),
                clock
        );
        assertThat(
                screeningFacade
                        .readScreening(sampleScreening.id())
                        .freeSeats()
        ).isEqualTo(freeSeatsBeforeBooking - 1);
    }

    @Test
    void should_cancel_ticket() {
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreening = addSampleScreening(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
        var sampleTicket = bookSampleTicket(sampleScreening.id());
        ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThat(
                ticketFacade
                        .read(sampleTicket.ticketUuid())
                        .status()
        ).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() {
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreening = addSampleScreening(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
        var sampleTicket = bookSampleTicket(sampleScreening.id());
        ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.systemUTC());
        assertThrows(
                TicketAlreadyCancelledException.class,
                () -> ticketFacade.cancel(sampleTicket.ticketUuid(), Clock.systemUTC())
        );
    }

    @Test
    void should_throw_exception_when_trying_cancel_ticket_when_there_is_less_than_24h_to_screening() {
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreening = addSampleScreening(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
        var sampleTicket = ticketFacade.book(
                sampleBookTicketDTO(sampleScreening.id()),
                clock
        );
        var lessThanOneDayBeforeScreening = sampleScreening
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
        var sampleFilm = addFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreening = addSampleScreening(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
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
