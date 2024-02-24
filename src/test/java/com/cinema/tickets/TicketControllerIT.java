package com.cinema.tickets;

import com.cinema.BaseIT;
import com.cinema.films.Film;
import com.cinema.films.FilmService;
import com.cinema.halls.Hall;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.HallService;
import com.cinema.halls.Seat;
import com.cinema.screenings.Screening;
import com.cinema.screenings.ScreeningService;
import com.cinema.screenings.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.exceptions.ScreeningSeatNotFoundException;
import com.cinema.tickets.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.exceptions.TicketBookTooLateException;
import com.cinema.tickets.exceptions.TicketCancelTooLateException;
import com.cinema.users.User;
import com.cinema.users.UserFixtures;
import com.cinema.users.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.cinema.films.FilmFixtures.createFilm;
import static com.cinema.screenings.ScreeningFixtures.createScreening;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TicketControllerIT extends BaseIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private HallService hallService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Clock clock;

    @Test
    void tickets_are_added() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var admin = addAdmin();

        webTestClient
                .post()
                .uri("/admin" + TICKETS_BASE_ENDPOINT + "?screeningId=" + screening.getId())
                .headers(headers -> headers.setBasicAuth(admin.getMail(), admin.getPassword()))
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient
                .get()
                .uri("/public" + TICKETS_BASE_ENDPOINT + "?screeningId=" + screening.getId())
                .exchange()
                .expectBody()
                .jsonPath("$").value(hasSize(hall.getSeats().size()))
                .jsonPath("$.*.filmTitle").value(everyItem(equalTo(film.getTitle())))
                .jsonPath("$.*.screeningDate").value(everyItem(equalTo(screening.getDate().toString())))
                .jsonPath("$.*.hallId").value(everyItem(equalTo(hall.getId().intValue())))
                .jsonPath("$.*.rowNumber").exists()
                .jsonPath("$.*.seatNumber").exists();
    }

    @Test
    void ticket_is_booked_for_existing_screening() {
        var user = addUser();
        var nonExistingScreeningId = 0L;
        var bookTicketDto = new TicketBookRequest(
                nonExistingScreeningId,
                List.of(new Seat(1, 1))
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.message", equalTo(new ScreeningNotFoundException().getMessage()));
    }

    @Test
    void ticket_is_booked_for_existing_seat() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var nonExistingSeatId = new Seat(0,0);
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(nonExistingSeatId)
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.message", equalTo(new ScreeningSeatNotFoundException().getMessage()));
    }

    @Test
    void ticket_is_booked() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var ticket = addTicket(screening);
        var user = addUser();
        var ticketBookRequest = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat())
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(ticketBookRequest)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(ticketRepository.findByIdAndUserId(1L, 1L))
                .isNotEmpty()
                .hasValueSatisfying(bookedTicket -> {
                    assertEquals(1L, bookedTicket.getUserId());
                    assertEquals(user.getId(), bookedTicket.getUserId());
                    assertEquals(ticketBookRequest.screeningId(), bookedTicket.getScreening().getId());
                    assertEquals(ticketBookRequest.seats().getFirst(), bookedTicket.getSeat());
                });
    }

    @Test
    void tickets_are_booked() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var tickets = addTickets(screening);
        var user = addUser();
        var seat1 = tickets.get(0).getSeat();
        var seat2 = tickets.get(1).getSeat();
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(seat1, seat2)
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(ticketRepository.findAllByUserId(user.getId()))
                .isNotEmpty()
                .allSatisfy(ticket -> assertEquals(user.getId(), ticket.getUserId()));
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user.getId());
        var bookTicketRequest = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat())
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketRequest)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(new TicketAlreadyBookedException().getMessage()));
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening);
        setCurrentDate(screening.getDate().plusMinutes(59));
        var bookTicketRequest = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat())
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketRequest)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(new TicketBookTooLateException().getMessage()));
    }

    @Test
    void ticket_is_cancelled() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user.getId());

        webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus().isOk();

        assertThat(ticketRepository.findById(ticket.getId()))
                .isNotEmpty()
                .hasValueSatisfying(cancelledTicket -> assertNull(cancelledTicket.getUserId()));
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        setCurrentDate(screening.getDate().plusHours(23));
        var user = addUser();
        var ticket = addTicket(screening, user.getId());

        webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(new TicketCancelTooLateException().getMessage()));
    }

    @Test
    void tickets_are_gotten_by_user_id() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user.getId());

        webTestClient
                .get()
                .uri(TICKETS_BASE_ENDPOINT + "/my")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$[0].id").isEqualTo(ticket.getId())
                .jsonPath("$[0].filmTitle").isEqualTo(film.getTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].hallId").isEqualTo(screening.getHallId())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().number())
                .jsonPath("$[0].userId").isEqualTo(user.getId());
    }

    @Test
    void tickets_are_gotten_by_screening_id() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var ticket = addTicket(screening);

        webTestClient
                .get()
                .uri("/public"+ TICKETS_BASE_ENDPOINT + "?screeningId=" + screening.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$[0].id").isEqualTo(ticket.getId())
                .jsonPath("$[0].filmTitle").isEqualTo(film.getTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].hallId").isEqualTo(screening.getHallId())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().number())
                .jsonPath("$[0].userId").value(Matchers.nullValue());
    }

    private Ticket addTicket(Screening screening) {
        return ticketRepository.save(TicketFixtures.createTicket(screening));
    }

    private Ticket addTicket(Screening screening, Long userId) {
        return ticketRepository.save(TicketFixtures.createTicket(screening, userId));
    }

    private List<Ticket> addTickets(Screening screening) {
        return ticketRepository.saveAll(
                List.of(
                        new Ticket(screening, new Seat(1, 1)),
                        new Ticket(screening, new Seat(1, 2))
                )
        );
    }

    private Screening addScreening(Long filmId, Long hallId) {
        return screeningService.addScreening(createScreening(filmId, hallId));
    }

    private Film addFilm() {
        return filmService.addFilm(createFilm());
    }

    private Hall addHall() {
        return hallService.createHall(HallFixtures.createHall());
    }

    private User addUser() {
        return userRepository.save(UserFixtures.createUser());
    }

    private User addAdmin() {
        return userRepository.save(UserFixtures.createUser(User.Role.ADMIN));
    }

    private void setCurrentDate(LocalDateTime date) {
        Mockito.when(clock.instant()).thenReturn(date.toInstant(ZoneOffset.UTC));
    }
}