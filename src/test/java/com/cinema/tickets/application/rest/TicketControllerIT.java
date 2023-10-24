package com.cinema.tickets.application.rest;

import com.cinema.SpringIT;
import com.cinema.films.application.services.FilmService;
import com.cinema.rooms.application.services.RoomService;
import com.cinema.screenings.application.services.ScreeningService;
import com.cinema.shared.events.EventPublisher;
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

import static com.cinema.tickets.TicketFixture.SCREENING_DATE;
import static com.cinema.tickets.TicketFixture.createCancelledTicket;
import static com.cinema.tickets.TicketFixture.createFilmCreateDto;
import static com.cinema.tickets.TicketFixture.createRoomCreateDto;
import static com.cinema.tickets.TicketFixture.createScreeningCrateDto;
import static com.cinema.tickets.TicketFixture.createTicket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
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
    private Clock clock;

    @MockBean
    private EventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        userService.createCommonUser(
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
        var seatId = 1L;
        var ticketBookDto = new TicketBookDto(
                nonExistingScreeningId,
                seatId
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
        addScreening();
        var screeningId = 1L;
        var nonExistingSeatId = 0L;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                nonExistingSeatId
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
        addScreening(filmTitle, roomId);
        var screeningId = 1L;
        var seatId = 1L;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                seatId
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
                    assertEquals(screeningId, ticket.getScreeningId());
                    assertEquals(seatId, ticket.getSeatId());
                    assertEquals(1L, ticket.getUserId());
                });
    }

    @Test
    void ticket_is_unique() {
        //given
        addScreening();
        var ticket = ticketRepository.add(createTicket());
        var ticketBookDto = new TicketBookDto(
                ticket.getScreeningId(),
                ticket.getSeatId()
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
        var screeningDate = SCREENING_DATE;
        addScreening(screeningDate);
        Mockito
                .when(clock.instant())
                .thenReturn(screeningDate.minusMinutes(59).toInstant(ZoneOffset.UTC));
        var screeningId = 1L;
        var seatId =  1L;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                seatId
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
        addScreening();
        var screeningId = 1L;
        var seatId = 1L;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                seatId
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
        var expectedEvent = new TicketBookedEvent(screeningId, seatId);
        verify(eventPublisher, times(1)).publish(expectedEvent);
    }

    @Test
    void ticket_is_cancelled() {
        //give
        addScreening();
        ticketRepository.add(createTicket());

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
        addScreening();
        var ticket = ticketRepository.add(createTicket());

        //when
        webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();

        //then
        var expectedEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getSeatId()
        );
        verify(eventPublisher, times(1)).publish(expectedEvent);
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() {
        //given
        addScreening();
        ticketRepository.add(createCancelledTicket());

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
        filmService.creteFilm(createFilmCreateDto());
        roomService.createRoom(createRoomCreateDto());
        var dto = createScreeningCrateDto();
        screeningService.createScreening(dto);

        ticketRepository.add(createTicket());
        Mockito
                .when(clock.instant())
                .thenReturn(dto.date().minusHours(23).toInstant(ZoneOffset.UTC));

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
        ticketRepository.add(createTicket(notCurrentUserId));

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
                .isEqualTo(HttpStatus.FORBIDDEN)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void tickets_are_read_by_user_id() {
        //given
        var filmCreateDto = createFilmCreateDto();
        filmService.creteFilm(filmCreateDto);

        var roomCreateDto = createRoomCreateDto();
        roomService.createRoom(roomCreateDto);

        var screeningCreateDto = createScreeningCrateDto();
        screeningService.createScreening(screeningCreateDto);

        var ticket = ticketRepository.add(createTicket());

        var rowNumber = 1;
        var seatNumber = 1;

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
                        filmCreateDto.title(),
                        screeningCreateDto.date(),
                        roomCreateDto.id(),
                        rowNumber,
                        seatNumber
                )
        );
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$[0].id").isEqualTo(expected.get(0).id())
                .jsonPath("$[0].status").isEqualTo(expected.get(0).status().name())
                .jsonPath("$[0].filmTitle").isEqualTo(expected.get(0).filmTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(expected.get(0).screeningDate().toString())
                .jsonPath("$[0].roomId").isEqualTo(expected.get(0).roomId())
                .jsonPath("$[0].rowNumber").isEqualTo(expected.get(0).rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(expected.get(0).seatNumber());
    }

    private void addScreening() {
        filmService.creteFilm(createFilmCreateDto());
        roomService.createRoom(createRoomCreateDto());
        screeningService.createScreening(createScreeningCrateDto(SCREENING_DATE));
    }

    private void addScreening(LocalDateTime screeningDate) {
        filmService.creteFilm(createFilmCreateDto());
        roomService.createRoom(createRoomCreateDto());
        screeningService.createScreening(createScreeningCrateDto(screeningDate));
    }

    private void addScreening(String filmTitle, String roomId) {
        filmService.creteFilm(
                createFilmCreateDto().withTitle(filmTitle)
        );
        roomService.createRoom(
                createRoomCreateDto().withId(roomId)
        );
        screeningService.createScreening(createScreeningCrateDto(SCREENING_DATE));
    }
}
