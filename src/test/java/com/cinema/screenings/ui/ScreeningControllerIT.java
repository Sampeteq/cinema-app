package com.cinema.screenings.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.halls.HallFixture;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.screenings.ScreeningFixture;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.cinema.screenings.ScreeningFixture.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixture.createScreening;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

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
                .contentType(MediaType.APPLICATION_JSON)
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
                    assertThat(screening.getHall().getId()).isEqualTo(1L);
                });
    }

    @Test
    void screening_and_current_date_difference_is_min_7_days() {
        //given
        var film = addFilm();
        var hall = addHall();
        var user = addUser();
        var screeningDate = LocalDateTime.now().plusDays(6);
        var createScreeningDto = new ScreeningCreateRequest(screeningDate, film.getId(), hall.getId());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
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
        var screeningDate = LocalDateTime.now().plusDays(23);
        var createScreeningDto = new ScreeningCreateRequest(screeningDate, film.getId(), hall.getId());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
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
        var screening = addScreening(film, hall);
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
                .contentType(MediaType.APPLICATION_JSON)
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
        var hall = addHall();
        var screening = addScreening(film, hall);
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
        var hall = addHall();
        var screening = addScreening(film, hall);

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
                .jsonPath("$.screenings[0].date").isEqualTo(screening.getDate().toString())
                .jsonPath("$.screenings[0].filmTitle").isEqualTo(film.getTitle());
    }

    @Test
    void screenings_are_gotten_by_date() {
        //given
        var film = addFilm();
        var hall = addHall();
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate, film, hall);
        addScreening(requiredDate.minusDays(1), film, hall);

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
                .jsonPath("$.screenings[*].date").isEqualTo(screeningWithRequiredDate.getDate().toString());
    }

    @Test
    void seats_are_gotten_by_screening_id() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithSeats(film, hall);

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SCREENINGS_PUBLIC_ENDPOINT + "/" + screening.getId() + "/seats")
                        .queryParam("screeningId", screening.getId())
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$.*.rowNumber").exists()
                .jsonPath("$.*.number").exists()
                .jsonPath("$.*.isFree").exists()
                .jsonPath("$.*.*").value(everyItem(notNullValue()));
    }

    private Film addFilm() {
        return filmRepository.save(FilmFixture.createFilm());
    }

    private Hall addHall() {
        return hallRepository.save(HallFixture.createHall());
    }

    private Screening addScreening(Film film, Hall hall) {
        return screeningRepository.save(createScreening(film, hall));
    }

    private Screening addScreening(LocalDate date, Film film, Hall hall) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime, film, hall);
        return screeningRepository.save(screening);
    }

    private Screening addScreeningWithSeats(Film film, Hall hall) {
        return screeningRepository.save(ScreeningFixture.createScreeningWithTickets(film, hall));
    }

    private User addUser() {
        return userRepository.save(UserFixture.createUser(User.Role.ADMIN));
    }
}
