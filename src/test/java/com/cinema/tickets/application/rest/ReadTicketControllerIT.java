package com.cinema.tickets.application.rest;

import com.cinema.SpringIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.rooms.application.commands.handlers.CreateRoomHandler;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.cinema.tickets.TicketFixture.createCreateFilmCommand;
import static com.cinema.tickets.TicketFixture.createCreateRoomCommand;
import static com.cinema.tickets.TicketFixture.createCreateScreeningCommand;
import static com.cinema.tickets.TicketFixture.createTicket;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

class ReadTicketControllerIT extends SpringIT {

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
    void tickets_are_read_by_user_id() {
        //given
        var createFilmCommand = createCreateFilmCommand();
        createFilmHandler.handle(createFilmCommand);

        var createRoomCommand = createCreateRoomCommand();
        createRoomHandler.handle(createRoomCommand);

        var createScreeningCommand = createCreateScreeningCommand();
        createScreeningHandler.handle(createScreeningCommand);

        var ticket = ticketRepository.add(createTicket());

        var rowNumber = 1;
        var seatNumber = 1;

        //when
        var spec = webTestClient
                .get()
                .uri(TICKETS_BASE_ENDPOINT + "/my")
                .headers(headers -> headers.setBasicAuth(username, password))
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
}
