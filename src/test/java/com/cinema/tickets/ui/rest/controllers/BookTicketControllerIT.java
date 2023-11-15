package com.cinema.tickets.ui.rest.controllers;

import com.cinema.SpringIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.rooms.application.commands.handlers.CreateRoomHandler;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.domain.SeatStatus;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.SeatAlreadyTakenException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
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

import static com.cinema.tickets.TicketFixture.SCREENING_DATE;
import static com.cinema.tickets.TicketFixture.createCreateFilmCommand;
import static com.cinema.tickets.TicketFixture.createCreateRoomCommand;
import static com.cinema.tickets.TicketFixture.createCreateScreeningCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTicketControllerIT extends SpringIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";
    private static final String username = "user1@mail.com";
    private static final String password = "12345";

    @Autowired
    private TicketRepository ticketRepository;

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
                        username,
                        password
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
                .headers(headers -> headers.setBasicAuth(username, password))
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
                .headers(headers -> headers.setBasicAuth(username, password))
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

    private void bookTicket(long screeningId, int rowNumber, int seatNumber) {
        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BookTicket(screeningId, rowNumber, seatNumber))
                .headers(headers -> headers.setBasicAuth(username, password))
                .exchange();
    }
}
