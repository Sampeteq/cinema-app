package com.cinema.tickets.infrastructure;

import com.cinema.BaseIT;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmService;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallService;
import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningService;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.tickets.TicketFixtures;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketReadRepository;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.users.UserFixtures;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static com.cinema.films.FilmFixtures.createFilmCreateDto;
import static com.cinema.screenings.ScreeningFixtures.createScreeningCreateDto;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TicketControllerIT extends BaseIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketReadRepository ticketReadRepository;

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

        assertThat(ticketReadRepository.getByScreeningId(screening.getId())).isNotEmpty();
    }

    @Test
    void ticket_is_booked_for_existing_screening() {
        var user = addUser();
        var nonExistingScreeningId = 0L;
        var ticketBookDto = new TicketBookDto(
                nonExistingScreeningId,
                List.of(new Seat(1, 1))
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$").isEqualTo(new ScreeningNotFoundException().getMessage());
    }

    @Test
    void ticket_is_booked_for_existing_seat() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var nonExistingSeatId = new Seat(0,0);
        var ticketBookDto = new TicketBookDto(
                screening.getId(),
                List.of(nonExistingSeatId)
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$").isEqualTo(new TicketNotFoundException().getMessage());
    }

    @Test
    void ticket_is_booked() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var ticket = addTicket(screening);
        var user = addUser();
        var ticketBookDto = new TicketBookDto(
                screening.getId(),
                List.of(ticket.getSeat())
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(ticketRepository.getByIdAndUserId(1L, 1L))
                .isNotEmpty()
                .hasValueSatisfying(bookedTicket -> {
                    assertEquals(user, bookedTicket.getUser());
                    assertEquals(user, bookedTicket.getUser());
                    assertEquals(ticketBookDto.screeningId(), bookedTicket.getScreening().getId());
                    assertEquals(ticketBookDto.seats().getFirst(), bookedTicket.getSeat());
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
        var ticketBookDto = new TicketBookDto(
                screening.getId(),
                List.of(seat1, seat2)
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(ticketRepository.getBySeat(seat1).orElseThrow().getUser()).isEqualTo(user);
        assertThat(ticketRepository.getBySeat(seat2).orElseThrow().getUser()).isEqualTo(user);
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user);
        var ticketBookDto = new TicketBookDto(
                screening.getId(),
                List.of(ticket.getSeat())
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$").isEqualTo(new TicketAlreadyBookedException().getMessage());
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening);
        setCurrentDate(screening.getDate().plusMinutes(59));
        var ticketBookDto = new TicketBookDto(
                screening.getId(),
                List.of(ticket.getSeat())
        );

        webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(ticketBookDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$").isEqualTo(new TicketBookTooLateException().getMessage());
    }

    @Test
    void ticket_is_cancelled() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user);

        webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus().isOk();

        assertThat(ticketRepository.getById(ticket.getId()))
                .isNotEmpty()
                .hasValueSatisfying(cancelledTicket -> assertNull(cancelledTicket.getUser()));
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        setCurrentDate(screening.getDate().plusHours(23));
        var user = addUser();
        var ticket = addTicket(screening, user);

        webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$").isEqualTo(new TicketCancelTooLateException().getMessage());
    }

    @Test
    void tickets_are_gotten_by_user_id() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user);

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
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().format(ISO_DATE_TIME))
                .jsonPath("$[0].hallId").isEqualTo(screening.getHall().getId())
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
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().format(ISO_DATE_TIME))
                .jsonPath("$[0].hallId").isEqualTo(screening.getHall().getId())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().number())
                .jsonPath("$[0].userId").value(Matchers.nullValue());
    }

    private Ticket addTicket(Screening screening) {
        return ticketRepository.add(TicketFixtures.createTicket(screening));
    }

    private Ticket addTicket(Screening screening, User user) {
        return ticketRepository.add(TicketFixtures.createTicket(screening, user));
    }

    private List<Ticket> addTickets(Screening screening) {
        return Stream.of(
                        new Ticket(screening, new Seat(1, 1)),
                        new Ticket(screening, new Seat(1, 2))
                )
                .map(ticketRepository::add)
                .toList();
    }

    private Screening addScreening(Long filmId, Long hallId) {
        return screeningService.createScreening(createScreeningCreateDto(filmId, hallId));
    }

    private Film addFilm() {
        return filmService.addFilm(createFilmCreateDto());
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
