package com.cinema.tickets.application.rest.controllers;

import com.cinema.SpringIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.rooms.application.commands.handlers.CreateRoomHandler;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUserException;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.ZoneOffset;

import static com.cinema.tickets.TicketFixture.SCREENING_DATE;
import static com.cinema.tickets.TicketFixture.createCancelledTicket;
import static com.cinema.tickets.TicketFixture.createCreateFilmCommand;
import static com.cinema.tickets.TicketFixture.createCreateRoomCommand;
import static com.cinema.tickets.TicketFixture.createCreateScreeningCommand;
import static com.cinema.tickets.TicketFixture.createTicket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CancelTicketControllerIT extends SpringIT {

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
        createFilmHandler.handle(createCreateFilmCommand());
        createRoomHandler.handle(createCreateRoomCommand());
        var command = createCreateScreeningCommand();
        createScreeningHandler.handle(command);

        ticketRepository.add(createTicket());
        Mockito
                .when(clock.instant())
                .thenReturn(command.date().minusHours(23).toInstant(ZoneOffset.UTC));

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
        var expectedMessage = new TicketNotBelongsToUserException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.FORBIDDEN)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    private void addScreening() {
        createFilmHandler.handle(createCreateFilmCommand());
        createRoomHandler.handle(createCreateRoomCommand());
        createScreeningHandler.handle(createCreateScreeningCommand(SCREENING_DATE));
    }
}
