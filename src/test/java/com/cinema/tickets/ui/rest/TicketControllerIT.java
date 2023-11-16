package com.cinema.tickets.ui.rest;

import com.cinema.BaseIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.rooms.application.commands.handlers.CreateRoomHandler;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.SeatStatus;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.SeatAlreadyTakenException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.repositories.SeatRepository;
import com.cinema.tickets.domain.repositories.TicketRepository;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.cinema.tickets.TicketFixture.SCREENING_DATE;
import static com.cinema.tickets.TicketFixture.createCancelledTicket;
import static com.cinema.tickets.TicketFixture.createCreateFilmCommand;
import static com.cinema.tickets.TicketFixture.createCreateRoomCommand;
import static com.cinema.tickets.TicketFixture.createCreateScreeningCommand;
import static com.cinema.tickets.TicketFixture.createSeat;
import static com.cinema.tickets.TicketFixture.createTicket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketControllerIT extends BaseIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";
    private static final String USERNAME = "user1@mail.com";
    private static final String PASSWORD = "12345";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private CreateFilmHandler createFilmHandler;

    @Autowired
    private CreateScreeningHandler createScreeningHandler;

    @Autowired
    private CreateRoomHandler createRoomHandler;

    @SpyBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        createUserHandler.handle(
                new CreateUser(
                        USERNAME,
                        PASSWORD
                )
        );
    }

    @Test
    void ticket_is_made_for_existing_screening() {
        //given
        var nonExistingScreeningId = 0L;
        var rowNumber = 1;
        var seatNumber = 1;
        var command = new BookTicket(
                nonExistingScreeningId,
                rowNumber,
                seatNumber
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isNotFound();
    }

    @Test
    void ticket_is_booked_for_existing_seat() {
        //given
        addScreening();
        var screeningId = 1L;
        var rowNumber = 1;
        var nonExistingSeatNumber = 0;
        var command = new BookTicket(
                screeningId,
                rowNumber,
                nonExistingSeatNumber
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
        var rowNumber = 1;
        var seatNumber = 1;
        var command = new BookTicket(
                screeningId,
                rowNumber,
                seatNumber
        );
        var ticketId = 1L;
        var userId = 1L;

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(ticketRepository.getByIdAndUserId(ticketId, userId))
                .isNotEmpty()
                .hasValueSatisfying(ticket -> {
                    assertEquals(TicketStatus.BOOKED, ticket.getStatus());
                    assertEquals(SeatStatus.TAKEN, ticket.getSeat().getStatus());
                    assertEquals(1L, ticket.getUserId());
                });
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        //given
        addScreening();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        bookTicket(screeningId, rowNumber, seatNumber);
        var command = new BookTicket(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        var expectedMessage = new SeatAlreadyTakenException().getMessage();
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
        var rowNumber = 1;
        var seatNumber = 1;
        var command = new BookTicket(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
    void ticket_is_cancelled() {
        //give
        addScreening();
        var seat = addSeat();
        var ticket = ticketRepository.add(createTicket(seat));

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.getByIdAndUserId(ticket.getId(), ticket.getUserId()))
                .isNotEmpty()
                .hasValueSatisfying(cancelledTicket ->
                        assertEquals(TicketStatus.CANCELLED, cancelledTicket.getStatus())
                );
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() {
        //given
        addScreening();
        var seat = addSeat();
        ticketRepository.add(createCancelledTicket(seat));

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
        createFilmHandler.handle(createCreateFilmCommand());
        createRoomHandler.handle(createCreateRoomCommand());
        var command = createCreateScreeningCommand();
        createScreeningHandler.handle(command);

        var seat = addSeat();
        ticketRepository.add(createTicket(seat));
        Mockito
                .when(clock.instant())
                .thenReturn(command.date().minusHours(23).toInstant(ZoneOffset.UTC));

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
    void tickets_are_gotten_by_user_id() {
        //given
        var createFilmCommand = createCreateFilmCommand();
        createFilmHandler.handle(createFilmCommand);

        var createRoomCommand = createCreateRoomCommand();
        createRoomHandler.handle(createRoomCommand);

        var createScreeningCommand = createCreateScreeningCommand();
        createScreeningHandler.handle(createScreeningCommand);

        var seat = addSeat();
        var ticket = ticketRepository.add(createTicket(seat));

        var rowNumber = 1;
        var seatNumber = 1;

        //when
        var spec = webTestClient
                .get()
                .uri(TICKETS_BASE_ENDPOINT + "/my")
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        var expected = List.of(
                new TicketDto(
                        1L,
                        ticket.getStatus(),
                        createFilmCommand.title(),
                        createScreeningCommand.date(),
                        createRoomCommand.id(),
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
        createFilmHandler.handle(createCreateFilmCommand());
        createRoomHandler.handle(createCreateRoomCommand());
        createScreeningHandler.handle(createCreateScreeningCommand(SCREENING_DATE));
    }

    private void addScreening(LocalDateTime screeningDate) {
        createFilmHandler.handle(createCreateFilmCommand());
        createRoomHandler.handle(createCreateRoomCommand());
        createScreeningHandler.handle(createCreateScreeningCommand(screeningDate));
    }

    private void addScreening(String filmTitle, String roomId) {
        createFilmHandler.handle(createCreateFilmCommand(filmTitle));
        createRoomHandler.handle(createCreateRoomCommand(roomId));
        createScreeningHandler.handle(createCreateScreeningCommand(SCREENING_DATE));
    }

    private Seat addSeat() {
        return seatRepository.add(createSeat());
    }

    private void bookTicket(long screeningId, int rowNumber, int seatNumber) {
        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BookTicket(screeningId, rowNumber, seatNumber))
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();
    }
}
