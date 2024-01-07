package com.cinema.tickets.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.halls.HallFixture;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.HallSeat;
import com.cinema.screenings.ScreeningFixture;
import com.cinema.screenings.ScreeningSeatFixture;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import com.cinema.tickets.TicketFixture;
import com.cinema.tickets.application.dto.BookTicketDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
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

import static com.cinema.tickets.TicketFixture.createCancelledTicket;
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
    private ScreeningRepository screeningRepository;

    @Autowired
    private ScreeningSeatRepository screeningSeatRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private Clock clock;

    private User user;

    @BeforeEach
    void setUpUser() {
        user = userRepository.add(UserFixture.createUser());
    }

    @Test
    void ticket_is_booked_for_existing_screening() {
        //given
        var nonExistingScreeningId = 0L;
        var seatId = 1L;
        var bookTicketDto = new BookTicketDto(
                nonExistingScreeningId,
                List.of(seatId)
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var nonExistingSeatId = 0L;
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(nonExistingSeatId)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat = addSeat(screening, hall.getSeats().getFirst());
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(seat.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(ticketRepository.getByIdAndUserId(1L, 1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket -> {
                    assertEquals(1L, ticket.getUser().getId());
                    assertEquals(TicketStatus.BOOKED, ticket.getStatus());
                    assertEquals(bookTicketDto.screeningId(), ticket.getSeat().getScreening().getId());
                    assertEquals(bookTicketDto.seatsIds().getFirst(), ticket.getSeat().getId());
                });
    }

    @Test
    void tickets_are_booked() {
        //given
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat1 = addSeat(screening, hall.getSeats().get(0));
        var seat2 = addSeat(screening, hall.getSeats().get(1));
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(seat1.getId(), seat2.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(ticketRepository.getAllByUserId(user.getId()))
                .isNotEmpty()
                .allSatisfy(ticket -> assertEquals(TicketStatus.BOOKED, ticket.status()));
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        //given
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat = addNotFreeSeat(screening, hall.getSeats().getFirst());
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(seat.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat = addSeat(screening, hall.getSeats().getFirst());
        Mockito
                .when(clock.instant())
                .thenReturn(screening.getDate().minusMinutes(59).toInstant(ZoneOffset.UTC));
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(seat.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat = addSeat(screening, hall.getSeats().getFirst());
        var ticket = addTicket(seat, user);

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.getByIdAndUserId(ticket.getId(), ticket.getUser().getId()))
                .isNotEmpty()
                .hasValueSatisfying(cancelledTicket ->
                        assertEquals(TicketStatus.CANCELLED, cancelledTicket.getStatus())
                );
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() {
        //given
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat = addSeat(screening, hall.getSeats().getFirst());
        var ticket = addCancelledTicket(seat, user);

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat = addSeat(screening, hall.getSeats().getFirst());
        var ticket = addTicket(seat, user);
        Mockito
                .when(clock.instant())
                .thenReturn(screening.getDate().minusHours(23).toInstant(ZoneOffset.UTC));

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var seat = addSeat(screening, hall.getSeats().getFirst());
        var ticket = addTicket(seat, user);

        //when
        var spec = webTestClient
                .get()
                .uri(TICKETS_BASE_ENDPOINT + "/my")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$.tickets[0].id").isEqualTo(ticket.getId())
                .jsonPath("$.tickets[0].status").isEqualTo(ticket.getStatus().name())
                .jsonPath("$.tickets[0].filmTitle").isEqualTo(film.getTitle())
                .jsonPath("$.tickets[0].screeningDate").isEqualTo(screening.getDate().toString())
                .jsonPath("$.tickets[0].hallId").isEqualTo(hall.getId())
                .jsonPath("$.tickets[0].rowNumber").isEqualTo(seat.getHallSeat().getRowNumber())
                .jsonPath("$.tickets[0].seatNumber").isEqualTo(seat.getHallSeat().getNumber());
    }

    private Ticket addTicket(ScreeningSeat seat, User user) {
        return ticketRepository.add(TicketFixture.createTicket(seat, user));
    }

    private Ticket addCancelledTicket(ScreeningSeat seat, User user) {
        return ticketRepository.add(createCancelledTicket(seat, user));
    }

    private Screening addScreening(Hall hall, Film film) {
        return screeningRepository.add(ScreeningFixture.createScreening(film, hall));
    }

    private ScreeningSeat addSeat(Screening screening, HallSeat hallSeat) {
        return screeningSeatRepository.add(ScreeningSeatFixture.createSeat(screening, hallSeat));
    }

    private ScreeningSeat addNotFreeSeat(Screening screening, HallSeat hallSeat) {
        return screeningSeatRepository.add(ScreeningSeatFixture.createNotFreeSeat(screening, hallSeat));
    }

    private Hall addHall() {
        return hallRepository.add(HallFixture.createHall());
    }

    private Film addFilm() {
        return filmRepository.add(FilmFixture.createFilm());
    }
}
