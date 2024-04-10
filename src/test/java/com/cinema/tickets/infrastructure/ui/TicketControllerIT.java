package com.cinema.tickets.infrastructure.ui;

import com.cinema.BaseIT;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallService;
import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningService;
import com.cinema.tickets.TicketFixtures;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketReadRepository;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.cinema.films.FilmFixtures.createFilmCreateDto;
import static com.cinema.halls.HallFixtures.createHallCreateDto;
import static com.cinema.screenings.ScreeningFixtures.createScreeningCreateDto;
import static com.cinema.users.UserFixtures.MAIL;
import static com.cinema.users.UserFixtures.PASSWORD;
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
    private UserService userService;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void tickets_are_added() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());

        webTestClient
                .post()
                .uri("/admin" + TICKETS_BASE_ENDPOINT + "?screeningId=" + screening.getId())
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(ticketReadRepository.getByScreeningId(screening.getId()))
                .isNotEmpty()
                .hasSize(hall.getSeats().size());
    }

    @Test
    @WithMockUser(username = "user1@mail.com")
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
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(ticketRepository.getByIdAndUserId(ticket.getId(), user.getId()))
                .isNotEmpty()
                .hasValueSatisfying(bookedTicket -> {
                    assertEquals(user.getId(), bookedTicket.getUserId());
                    assertEquals(ticketBookDto.screeningId(), bookedTicket.getScreening().getId());
                    assertEquals(ticketBookDto.seats().getFirst(), bookedTicket.getSeat());
                });
    }

    @Test
    @WithMockUser(username = "user1@mail.com")
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
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(
                ticketRepository
                        .getByScreeningIdAndSeat(screening.getId(), seat1)
                        .orElseThrow()
                        .getUserId()
        ).isEqualTo(user.getId());
        assertThat(
                ticketRepository
                        .getByScreeningIdAndSeat(screening.getId(), seat2)
                        .orElseThrow()
                        .getUserId()
        ).isEqualTo(user.getId());
    }

    @Test
    @WithMockUser(username = "user1@mail.com")
    void ticket_is_cancelled() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user);

        webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .exchange()
                .expectStatus().isOk();

        assertThat(ticketRepository.getById(ticket.getId()))
                .isNotEmpty()
                .hasValueSatisfying(cancelledTicket -> assertNull(cancelledTicket.getUserId()));
    }

    @Test
    @WithMockUser(username = "user1@mail.com")
    void tickets_are_gotten_by_user_id() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film.getId(), hall.getId());
        var user = addUser();
        var ticket = addTicket(screening, user);

        webTestClient
                .get()
                .uri(TICKETS_BASE_ENDPOINT + "/my")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$[0].id").isEqualTo(ticket.getId().toString())
                .jsonPath("$[0].filmTitle").isEqualTo(film.getTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().format(ISO_DATE_TIME))
                .jsonPath("$[0].hallId").isEqualTo(screening.getHallId().toString())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().number())
                .jsonPath("$[0].userId").isEqualTo(user.getId().toString());
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
                .jsonPath("$[0].id").isEqualTo(ticket.getId().toString())
                .jsonPath("$[0].filmTitle").isEqualTo(film.getTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().format(ISO_DATE_TIME))
                .jsonPath("$[0].hallId").isEqualTo(screening.getHallId().toString())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().number())
                .jsonPath("$[0].userId").value(Matchers.nullValue());
    }

    private Ticket addTicket(Screening screening) {
        return ticketRepository.save(TicketFixtures.createTicket(screening));
    }

    private Ticket addTicket(Screening screening, User user) {
        return ticketRepository.save(TicketFixtures.createTicket(screening, user));
    }

    private List<Ticket> addTickets(Screening screening) {
        return Stream.of(
                        new Ticket(UUID.randomUUID(), screening, new Seat(1, 1), null, 0),
                        new Ticket(UUID.randomUUID(), screening, new Seat(1, 2), null, 0)
                )
                .map(ticketRepository::save)
                .toList();
    }

    private Screening addScreening(UUID filmId, UUID hallId) {
        return screeningService.createScreening(createScreeningCreateDto(filmId, hallId));
    }

    private Film addFilm() {
        return filmService.createFilm(createFilmCreateDto());
    }

    private Hall addHall() {
        return hallService.createHall(createHallCreateDto());
    }

    private User addUser() {
        return userService.createUser(MAIL, PASSWORD);
    }
}
