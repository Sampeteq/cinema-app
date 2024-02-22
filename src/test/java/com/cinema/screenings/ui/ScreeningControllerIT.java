package com.cinema.screenings.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixtures;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.screenings.domain.Screening;
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
import static com.cinema.screenings.ScreeningFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

class ScreeningControllerIT extends BaseIT {

    private static final String SCREENINGS_ADMIN_ENDPOINT = "/admin/screenings";
    private static final String SCREENINGS_PUBLIC_ENDPOINT = "/public/screenings";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void screening_is_created() {
        //given
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var createScreeningDto = new ScreeningCreateRequest(SCREENING_DATE, film.getId(), hall.getId());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(screeningRepository.findById(1L))
                .isNotEmpty()
                .hasValueSatisfying(screening -> {
                    assertThat(screening.getDate()).isEqualTo(createScreeningDto.date());
                    assertThat(screening.getFilm().getId()).isEqualTo(createScreeningDto.filmId());
                    assertThat(screening.getHallId()).isEqualTo(1L);
                });
    }

    @Test
    void screening_and_current_date_difference_is_min_7_days() {
        //given
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var createScreeningDto = new ScreeningCreateRequest(
                CURRENT_DATE.plusDays(6),
                film.getId(),
                hall.getId()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new ScreeningDateOutOfRangeException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void screening_and_current_date_difference_is_max_21_days() {
        //given
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var createScreeningDto = new ScreeningCreateRequest(
                CURRENT_DATE.plusDays(22),
                film.getId(),
                hall.getId()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new ScreeningDateOutOfRangeException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void screenings_collision_cannot_exist() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall.getId());
        var user = addUser();
        var createScreeningDto = new ScreeningCreateRequest(
                screening.getDate(),
                film.getId(),
                hall.getId()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new ScreeningsCollisionsException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void screening_is_deleted() {
        //given
        var film = addFilm();
        var screening = addScreening(film);
        var user = addUser();

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_ADMIN_ENDPOINT + "/" + screening.getId())
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(screeningRepository.findById(screening.getId())).isEmpty();
    }

    @Test
    void screenings_are_gotten() {
        //given
        var film = addFilm();
        var screening = addScreening(film);

        //when
        var spec = webTestClient
                .get()
                .uri(SCREENINGS_PUBLIC_ENDPOINT)
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(hasSize(1))
                .jsonPath("$[*].*").value(everyItem(notNullValue()))
                .jsonPath("$[0].date").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].filmTitle").isEqualTo(film.getTitle());
    }

    @Test
    void screenings_are_gotten_by_date() {
        //given
        var film = addFilm();
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate, film);
        addScreening(requiredDate.minusDays(1), film);

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SCREENINGS_PUBLIC_ENDPOINT)
                        .queryParam("date", requiredDate.toString())
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*].date").isEqualTo(screeningWithRequiredDate.getDate().toString());
    }

    private Film addFilm() {
        return filmRepository.save(FilmFixtures.createFilm());
    }

    private Hall addHall() {
        return hallRepository.save(HallFixtures.createHall());
    }

    private Screening addScreening(Film film) {
        return screeningRepository.save(createScreening(film));
    }

    private Screening addScreening(Film film, Long hallId) {
        return screeningRepository.save(createScreening(film, hallId));
    }

    private Screening addScreening(LocalDate date, Film film) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime, film);
        return screeningRepository.save(screening);
    }

    private User addUser() {
        return userRepository.save(UserFixtures.createUser(User.Role.ADMIN));
    }
}
