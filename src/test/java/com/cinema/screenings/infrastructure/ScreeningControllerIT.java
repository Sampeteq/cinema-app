package com.cinema.screenings.infrastructure;

import com.cinema.BaseIT;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmService;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallService;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningCreateDto;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import com.cinema.users.UserFixtures;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static com.cinema.ClockFixtures.CURRENT_DATE;
import static com.cinema.films.FilmFixtures.createFilmCreateDto;
import static com.cinema.screenings.ScreeningFixtures.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixtures.createScreening;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

class ScreeningControllerIT extends BaseIT {

    private static final String SCREENINGS_ADMIN_ENDPOINT = "/admin/screenings";
    private static final String SCREENINGS_PUBLIC_ENDPOINT = "/public/screenings";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private FilmService filmService;

    @Autowired
    private HallService hallService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void screening_is_created() {
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var screeningCreateDto = new ScreeningCreateDto(SCREENING_DATE, film.getId(), hall.getId());

        //when
        webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.date").isEqualTo(screeningCreateDto.date().format(ISO_DATE_TIME))
                .jsonPath("$.filmId").isEqualTo(film.getId())
                .jsonPath("$.hallId").isEqualTo(screeningCreateDto.hallId());
    }

    @Test
    void screening_and_current_date_difference_is_min_7_days() {
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var screeningCreateDto = new ScreeningCreateDto(
                CURRENT_DATE.plusDays(6),
                film.getId(),
                hall.getId()
        );

        webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(new ScreeningDateOutOfRangeException().getMessage()));
    }

    @Test
    void screening_and_current_date_difference_is_max_21_days() {
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var screeningCreateDto = new ScreeningCreateDto(
                CURRENT_DATE.plusDays(22),
                film.getId(),
                hall.getId()
        );

        webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(new ScreeningDateOutOfRangeException().getMessage()));
    }

    @Test
    void screenings_collision_cannot_exist() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var user = addUser();
        var screeningCreateDto = new ScreeningCreateDto(
                screening.getDate(),
                film.getId(),
                hall.getId()
        );

        webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(new ScreeningsCollisionsException().getMessage()));
    }

    @Test
    void screening_is_deleted() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var user = addUser();

        webTestClient
                .delete()
                .uri(SCREENINGS_ADMIN_ENDPOINT + "/" + screening.getId())
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isNoContent();

        assertThat(screeningRepository.getById(screening.getId())).isEmpty();
    }

    @Test
    void screenings_are_gotten() {
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);

        webTestClient
                .get()
                .uri(SCREENINGS_PUBLIC_ENDPOINT)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*].*").value(everyItem(notNullValue()))
                .jsonPath("$[0].id").isEqualTo(screening.getId())
                .jsonPath("$[0].date").isEqualTo(screening.getDate().format(ISO_DATE_TIME))
                .jsonPath("$[0].filmId").isEqualTo(screening.getFilm().getId())
                .jsonPath("$[0].hallId").isEqualTo(screening.getHall().getId());
    }

    @Test
    void screenings_are_gotten_by_date() {
        var film = addFilm();
        var hall = addHall();
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate, film, hall);
        addScreening(requiredDate.minusDays(1), film, hall);

        webTestClient
                .get()
                .uri(SCREENINGS_PUBLIC_ENDPOINT + "?date=" + requiredDate)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*].date").isEqualTo(screeningWithRequiredDate.getDate().format(ISO_DATE_TIME));
    }

    private Film addFilm() {
        return filmService.createFilm(createFilmCreateDto());
    }

    private Hall addHall() {
        return hallService.createHall(HallFixtures.createHall());
    }

    private Screening addScreening(Film film, Hall hall) {
        return screeningRepository.save(createScreening(film, hall));
    }

    private Screening addScreening(LocalDate date, Film film, Hall hall) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime, film, hall);
        return screeningRepository.save(screening);
    }

    private User addUser() {
        return userRepository.save(UserFixtures.createUser(User.Role.ADMIN));
    }
}
