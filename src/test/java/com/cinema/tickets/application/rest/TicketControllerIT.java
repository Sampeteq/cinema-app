package com.cinema.tickets.application.rest;

import com.cinema.SpringIT;
import com.cinema.repertoire.application.services.FilmService;
import com.cinema.repertoire.application.services.ScreeningService;
import com.cinema.rooms.application.services.RoomService;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.TicketTestHelper;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.cinema.repertoire.ScreeningTestHelper.getScreeningDate;
import static com.cinema.tickets.TicketTestHelper.createFilmCreateDto;
import static com.cinema.tickets.TicketTestHelper.createRoomCreateDto;
import static com.cinema.tickets.TicketTestHelper.createScreeningCrateDto;
import static com.cinema.tickets.TicketTestHelper.prepareBookedTicket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TicketControllerIT extends SpringIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";
    private static final String username = "user1@mail.com";
    private static final String password = "12345";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private RoomService roomService;

    @SpyBean
    private Clock clockMock;

    @MockBean
    private EventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        userService.createUser(
                new UserCreateDto(
                        username,
                        password,
                        password
                )
        );
    }

    @Test
    void ticket_is_made_for_existing_screening() {
        //given
        var nonExistingScreeningId = 0L;
        var seatRowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                nonExistingScreeningId,
                seatRowNumber,
                seatNumber
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        spec.expectStatus().isNotFound();
    }

    @Test
    void ticket_is_booked_for_existing_seat() {
        //given
        prepareSeat();
        var screeningId = 1L;
        var nonExistingSeatRowNumber = 100;
        var nonExistingSeatNumber = 100;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                nonExistingSeatRowNumber,
                nonExistingSeatNumber
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        spec.expectStatus().isNotFound();
    }

    @Test
    void ticket_is_booked() {
        //given
        var filmTitle = "Title 1";
        var roomId = "1";
        var screeningDate = getScreeningDate(clockMock);
        prepareSeat(filmTitle, roomId, screeningDate);
        var screeningId = 1L;
        var seatRowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                seatRowNumber,
                seatNumber
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(ticketRepository.readById(1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket -> {
                    assertEquals(TicketStatus.ACTIVE, ticket.getStatus());
                    assertEquals(filmTitle, ticket.getFilmTitle());
                    assertEquals(screeningDate, ticket.getScreeningDate());
                    assertEquals(screeningId, ticket.getScreeningId());
                    assertEquals(roomId, ticket.getRoomId());
                    assertEquals(seatRowNumber, ticket.getSeatNumber());
                    assertEquals(seatNumber, ticket.getSeatNumber());
                    assertEquals(1L, ticket.getUserId());
                });
    }

    @Test
    void ticket_is_unique() {
        //given
        prepareSeat();
        var ticket = ticketRepository.add(TicketTestHelper.prepareTicket());
        var ticketBookDto = new TicketBookDto(
                ticket.getScreeningId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedMessage = new TicketAlreadyExists().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() {
        //given
        var screeningDate = getScreeningDate(clockMock);
        prepareSeat(screeningDate);
        Mockito
                .when(clockMock.instant())
                .thenReturn(screeningDate.minusMinutes(59).toInstant(ZoneOffset.UTC));
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedMessage = new TicketBookTooLateException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_booked_event_is_published() {
        //given
        prepareSeat();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedEvent = new TicketBookedEvent(screeningId, rowNumber, seatNumber);
        verify(eventPublisher, times(1)).publish(expectedEvent);
    }

    @Test
    void ticket_is_cancelled() {
        //give
        prepareSeat();
        ticketRepository.add(prepareBookedTicket());

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.readById(1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket ->
                        assertEquals(TicketStatus.CANCELLED, ticket.getStatus())
                );
    }

    @Test
    void ticket_cancelled_event_is_published() {
        //given
        prepareSeat();
        var ticket = ticketRepository.add(TicketTestHelper.prepareBookedTicket());

        //when
        webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber()
        );
        verify(eventPublisher, times(1)).publish(expectedEvent);
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() {
        //given
        prepareSeat();
        ticketRepository.add(TicketTestHelper.prepareCancelledTicket());

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedMessage = new TicketAlreadyCancelledException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        //given
        var screeningDate = getScreeningDate(clockMock);
        ticketRepository.add(TicketTestHelper.prepareBookedTicket(screeningDate));
        Mockito
                .when(clockMock.instant())
                .thenReturn(screeningDate.minusHours(23).toInstant(ZoneOffset.UTC));

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedMessage = new TicketCancelTooLateException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_cancelled_if_belongs_to_current_user() {
        //given
        var notCurrentUserId = 2L;
        ticketRepository.add(prepareBookedTicket(notCurrentUserId));

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedMessage = new TicketNotBelongsToUser().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void tickets_are_read_by_user_id() {
        //given
        var ticket = ticketRepository.add(TicketTestHelper.prepareBookedTicket());

        //when
        var spec = webTestClient
                .get()
                .uri("/tickets/my")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expected = List.of(
                new TicketDto(
                        1L,
                        ticket.getStatus(),
                        ticket.getFilmTitle(),
                        ticket.getScreeningDate(),
                        ticket.getRoomId(),
                        ticket.getRowNumber(),
                        ticket.getSeatNumber()
                )
        );
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .json(toJson(expected));
    }

    private void prepareSeat() {
        filmService.creteFilm(createFilmCreateDto());
        roomService.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(clockMock);
        screeningService.createScreening(createScreeningCrateDto(screeningDate));
    }

    private void prepareSeat(LocalDateTime screeningDate) {
        filmService.creteFilm(createFilmCreateDto());
        roomService.createRoom(createRoomCreateDto());
        screeningService.createScreening(createScreeningCrateDto(screeningDate));
    }

    private void prepareSeat(String filmTitle, String roomId, LocalDateTime screeningDate) {
        filmService.creteFilm(
                createFilmCreateDto().withTitle(filmTitle)
        );
        roomService.createRoom(
                createRoomCreateDto().withId(roomId)
        );
        screeningService.createScreening(createScreeningCrateDto(screeningDate));
    }
}
