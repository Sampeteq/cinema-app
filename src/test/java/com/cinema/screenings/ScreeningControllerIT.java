package com.cinema.screenings;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixtures;
import com.cinema.films.Film;
import com.cinema.films.FilmService;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.Hall;
import com.cinema.halls.HallRepository;
import com.cinema.screenings.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.screenings.exceptions.ScreeningsCollisionsException;
import com.cinema.users.UserFixtures;
import com.cinema.users.User;
import com.cinema.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static com.cinema.ClockFixtures.CURRENT_DATE;
import static com.cinema.screenings.ScreeningFixtures.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixtures.createScreening;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

class ScreeningControllerIT extends BaseIT {

    private static final String SCREENINGS_ADMIN_ENDPOINT = "/admin/screenings";
    private static final String SCREENINGS_PUBLIC_ENDPOINT = "/public/screenings";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private FilmService filmService;

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
        var screening = new Screening(SCREENING_DATE, film.getTitle(), hall.getId());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(screening)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(screeningRepository.findById(1L))
                .isNotEmpty()
                .hasValueSatisfying(foundScreening -> {
                    assertThat(foundScreening.getDate()).isEqualTo(screening.getDate());
                    assertThat(foundScreening.getFilmTitle()).isEqualTo(screening.getFilmTitle());
                    assertThat(foundScreening.getHallId()).isEqualTo(1L);
                });
    }

    @Test
    void screening_and_current_date_difference_is_min_7_days() {
        //given
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var screening = new Screening(
                CURRENT_DATE.plusDays(6),
                film.getTitle(),
                hall.getId()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(screening)
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
        var screening = new Screening(
                CURRENT_DATE.plusDays(22),
                film.getTitle(),
                hall.getId()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(screening)
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
        var screening = addScreening(film.getTitle(), hall.getId());
        var user = addUser();
        var otherScreening = new Screening(
                screening.getDate(),
                film.getTitle(),
                hall.getId()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .bodyValue(otherScreening)
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
        var screening = addScreening();
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
        var screening = addScreening();

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
                .jsonPath("$[0].filmTitle").isEqualTo(screening.getFilmTitle());
    }

    @Test
    void screenings_are_gotten_by_date() {
        //given
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate);
        addScreening(requiredDate.minusDays(1));

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
        return filmService.addFilm(FilmFixtures.createFilm());
    }

    private Hall addHall() {
        return hallRepository.save(HallFixtures.createHall());
    }

    private Screening addScreening() {
        return screeningRepository.save(createScreening());
    }

    private Screening addScreening(String filmTitle, Long hallId) {
        return screeningRepository.save(createScreening(filmTitle, hallId));
    }

    private Screening addScreening(LocalDate date) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime);
        return screeningRepository.save(screening);
    }

    private User addUser() {
        return userRepository.save(UserFixtures.createUser(User.Role.ADMIN));
    }
}
