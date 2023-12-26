package com.cinema.tickets.ui;

import com.cinema.BaseIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.halls.infrastructure.config.CreateHallService;
import com.cinema.screenings.ScreeningFixture;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
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
import java.time.ZoneOffset;
import java.util.List;

import static com.cinema.films.FilmFixture.createCreateFilmCommand;
import static com.cinema.halls.HallFixture.createCreateHallCommand;
import static com.cinema.screenings.ScreeningFixture.createCreateScreeningCommand;
import static com.cinema.tickets.TicketFixture.createCancelledTicket;
import static com.cinema.tickets.TicketFixture.createTicket;
import static com.cinema.users.UserFixture.createCrateUserCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketControllerIT extends BaseIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CreateFilmHandler createFilmHandler;

    @Autowired
    private CreateHallService createHallService;

    @Autowired
    private CreateScreeningHandler createScreeningHandler;

    @Autowired
    private CreateUserHandler createUserHandler;

    private final CreateUser createUserCommand = createCrateUserCommand();

    @SpyBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        createUserHandler.handle(createUserCommand);
    }

    @Test
    void ticket_is_booked_for_existing_screening() {
        //given
        var nonExistingScreeningId = 0L;
        var seatId = 1L;
        var command = new BookTicket(
                nonExistingScreeningId,
                List.of(seatId)
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();

        //then
        var expectedMessage = new ScreeningNotFoundException().getMessage();
        spec
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked_for_existing_seat() {
        //given
        addFilm();
        addHall();
        addScreening();
        var screeningId = 1L;
        var nonExistingSeatId = 0L;
        var command = new BookTicket(
                screeningId,
                List.of(nonExistingSeatId)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();

        //then
        var expectedMessage = new ScreeningSeatNotFoundException().getMessage();
        spec
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked() {
        //given
        addFilm();
        addHall();
        addScreening();
        var screeningId = 1L;
        var seatId = 1L;
        var command = new BookTicket(
                screeningId,
                List.of(seatId)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(ticketRepository.getByIdAndUserId(1L, 1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket -> {
                    assertEquals(1L, ticket.getUserId());
                    assertEquals(TicketStatus.BOOKED, ticket.getStatus());
                    assertEquals(command.screeningId(), ticket.getScreeningId());
                    assertEquals(command.seatsIds().getFirst(), ticket.getSeatId());
                });
    }

    @Test
    void tickets_are_booked() {
        //given
        addFilm();
        addHall();
        addScreening();
        var screeningId = 1L;
        var command = new BookTicket(
                screeningId,
                List.of(1L,2L)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(ticketRepository.getAllByUserId(1L))
                .isNotEmpty()
                .allSatisfy(ticket -> {
                    assertEquals(TicketStatus.BOOKED, ticket.getStatus());
                    assertEquals(1L, ticket.getUserId());
                });
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        //given
        addFilm();
        addHall();
        addScreening();
        var screeningId = 1L;
        var seatId = 1L;
        bookTicket(screeningId, seatId);
        var command = new BookTicket(
                screeningId,
                List.of(seatId)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();

        //then
        var expectedMessage = new TicketAlreadyExistsException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() {
        //given
        addFilm();
        addHall();
        addScreening();
        Mockito
                .when(clock.instant())
                .thenReturn(ScreeningFixture.SCREENING_DATE.minusMinutes(59).toInstant(ZoneOffset.UTC));
        var screeningId = 1L;
        var seatId = 1L;
        var command = new BookTicket(
                screeningId,
                List.of(seatId)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
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
        addFilm();
        addHall();
        addScreening();
        var ticket = ticketRepository.add(createTicket());

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
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
        addFilm();
        addHall();
        addScreening();
        ticketRepository.add(createCancelledTicket());

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
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
        addFilm();
        addHall();
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
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
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

        var createHallCommand = createCreateHallCommand();
        createHallService.handle(createHallCommand);

        var createScreeningCommand = createCreateScreeningCommand();
        createScreeningHandler.handle(createScreeningCommand);

        var ticket = ticketRepository.add(createTicket());

        //when
        var spec = webTestClient
                .get()
                .uri(TICKETS_BASE_ENDPOINT + "/my")
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();

        //then
        var expectedTicketDto = List.of(
                new TicketDto(
                        1L,
                        ticket.getStatus(),
                        createFilmCommand.title(),
                        createScreeningCommand.date(),
                        1L,
                        1,
                        1
                )
        );
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$.tickets[0].id").isEqualTo(expectedTicketDto.getFirst().id())
                .jsonPath("$.tickets[0].status").isEqualTo(expectedTicketDto.getFirst().status().name())
                .jsonPath("$.tickets[0].filmTitle").isEqualTo(expectedTicketDto.getFirst().filmTitle())
                .jsonPath("$.tickets[0].screeningDate").isEqualTo(expectedTicketDto.getFirst().screeningDate().toString())
                .jsonPath("$.tickets[0].hallId").isEqualTo(expectedTicketDto.getFirst().hallId())
                .jsonPath("$.tickets[0].rowNumber").isEqualTo(expectedTicketDto.getFirst().rowNumber())
                .jsonPath("$.tickets[0].seatNumber").isEqualTo(expectedTicketDto.getFirst().seatNumber());
    }

    private void addFilm() {
        createFilmHandler.handle(createCreateFilmCommand());
    }

    private void addHall() {
        createHallService.handle(createCreateHallCommand());
    }

    private void addScreening() {
        createScreeningHandler.handle(createCreateScreeningCommand(ScreeningFixture.SCREENING_DATE));
    }

    private void bookTicket(long screeningId, long seatId) {
        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BookTicket(screeningId, List.of(seatId)))
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();
    }
}
