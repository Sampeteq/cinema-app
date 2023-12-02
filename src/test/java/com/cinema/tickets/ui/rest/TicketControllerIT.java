package com.cinema.tickets.ui.rest;

import com.cinema.BaseIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.halls.infrastructure.config.CreateHallService;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.application.commands.dto.SeatPositionDto;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.SeatStatus;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.SeatAlreadyTakenException;
import com.cinema.tickets.domain.exceptions.SeatNotFoundException;
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

import static com.cinema.films.FilmFixture.createCreateFilmCommand;
import static com.cinema.halls.HallFixture.createCreateHallCommand;
import static com.cinema.screenings.ScreeningFixture.createCreateScreeningCommand;
import static com.cinema.tickets.TicketFixture.SCREENING_DATE;
import static com.cinema.tickets.TicketFixture.createCancelledTicket;
import static com.cinema.tickets.TicketFixture.createSeat;
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
    private SeatRepository seatRepository;

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
        var rowNumber = 1;
        var seatNumber = 1;
        var command = new BookTicket(
                nonExistingScreeningId,
                List.of(new SeatPositionDto(rowNumber, seatNumber))
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
        addScreening();
        var screeningId = 1L;
        var rowNumber = 1;
        var nonExistingSeatNumber = 0;
        var command = new BookTicket(
                screeningId,
                List.of(new SeatPositionDto(rowNumber, nonExistingSeatNumber))
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
        var expectedMessage = new SeatNotFoundException().getMessage();
        spec
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked() {
        //given
        addScreening();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var command = new BookTicket(
                screeningId,
                List.of(new SeatPositionDto(rowNumber, seatNumber))
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
                    assertEquals(TicketStatus.BOOKED, ticket.getStatus());
                    assertEquals(SeatStatus.TAKEN, ticket.getSeat().getStatus());
                    assertEquals(1L, ticket.getUserId());
                });
    }

    @Test
    void tickets_are_booked() {
        //given
        addScreening();
        var screeningId = 1L;
        var command = new BookTicket(
                screeningId,
                List.of(
                        new SeatPositionDto(1, 1),
                        new SeatPositionDto(1,2)
                )
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
                List.of(new SeatPositionDto(rowNumber, seatNumber))
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
                List.of(new SeatPositionDto(rowNumber, seatNumber))
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
        addScreening();
        var seat = addSeat();
        var ticket = ticketRepository.add(createTicket(seat));

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
        addScreening();
        var seat = addSeat();
        ticketRepository.add(createCancelledTicket(seat));

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

        var seat = addSeat();
        ticketRepository.add(createTicket(seat));
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

        var seat = addSeat();
        var ticket = ticketRepository.add(createTicket(seat));

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
                        seat.getRowNumber(),
                        seat.getNumber()
                )
        );
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$[0].id").isEqualTo(expectedTicketDto.get(0).id())
                .jsonPath("$[0].status").isEqualTo(expectedTicketDto.get(0).status().name())
                .jsonPath("$[0].filmTitle").isEqualTo(expectedTicketDto.get(0).filmTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(expectedTicketDto.get(0).screeningDate().toString())
                .jsonPath("$[0].hallId").isEqualTo(expectedTicketDto.get(0).hallId())
                .jsonPath("$[0].rowNumber").isEqualTo(expectedTicketDto.get(0).rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(expectedTicketDto.get(0).seatNumber());
    }

    private void addFilm() {
        createFilmHandler.handle(createCreateFilmCommand());
    }

    private void addHall() {
        createHallService.handle(createCreateHallCommand());
    }

    private void addScreening() {
        addFilm();
        addHall();
        createScreeningHandler.handle(createCreateScreeningCommand(SCREENING_DATE));
    }

    private void addScreening(LocalDateTime screeningDate) {
        addFilm();
        addHall();
        createScreeningHandler.handle(createCreateScreeningCommand(screeningDate));
    }

    private Seat addSeat() {
        return seatRepository.add(createSeat());
    }

    private void bookTicket(long screeningId, int rowNumber, int seatNumber) {
        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BookTicket(screeningId, List.of(new SeatPositionDto(rowNumber, seatNumber))))
                .headers(headers -> headers.setBasicAuth(createUserCommand.mail(), createUserCommand.password()))
                .exchange();
    }
}
