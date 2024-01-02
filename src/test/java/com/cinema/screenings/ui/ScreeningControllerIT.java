package com.cinema.screenings.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.application.FilmService;
import com.cinema.halls.application.HallService;
import com.cinema.screenings.ScreeningSeatFixture;
import com.cinema.screenings.application.dto.CreateScreeningDto;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import com.cinema.users.UserFixture;
import com.cinema.users.application.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.cinema.halls.HallFixture.createCreateHallDto;
import static com.cinema.screenings.ScreeningFixture.HALL_ID;
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
    private ScreeningSeatRepository screeningSeatRepository;

    @Autowired
    private FilmService filmService;

    @Autowired
    private HallService hallService;

    @Autowired
    private UserService userService;

    @Test
    void screening_is_created() {
        //given
        addHall();
        var filmId = 1L;
        addFilm();
        var crateUserDto = UserFixture.createCrateUserDto();
        userService.createAdmin(crateUserDto);
        var createScreeningDto = new CreateScreeningDto(SCREENING_DATE, filmId, HALL_ID);

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(crateUserDto.mail(), crateUserDto.password()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(screeningRepository.getById(1L))
                .isNotEmpty()
                .hasValueSatisfying(screening -> {
                    assertThat(screening.getDate()).isEqualTo(createScreeningDto.date());
                    assertThat(screening.getFilmId()).isEqualTo(createScreeningDto.filmId());
                    assertThat(screening.getHallId()).isEqualTo(1L);
                });
    }

    @Test
    void screening_and_current_date_difference_is_min_7_days() {
        //given
        addHall();
        var filmId = 1L;
        addFilm();
        var crateUserDto = UserFixture.createCrateUserDto();
        userService.createAdmin(crateUserDto);
        var screeningDate = LocalDateTime.now().plusDays(6);
        var createScreeningDto = new CreateScreeningDto(screeningDate, filmId, HALL_ID);

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(crateUserDto.mail(), crateUserDto.password()))
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
        addHall();
        var filmId = 1L;
        addFilm();
        var crateUserDto = UserFixture.createCrateUserDto();
        userService.createAdmin(crateUserDto);
        var screeningDate = LocalDateTime.now().plusDays(23);
        var createScreeningDto = new CreateScreeningDto(screeningDate, filmId, HALL_ID);

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(crateUserDto.mail(), crateUserDto.password()))
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
        var filmId = 1L;
        addFilm();
        addHall();
        var screening = addScreening();
        var crateUserDto = UserFixture.createCrateUserDto();
        userService.createAdmin(crateUserDto);
        var createScreeningDto = new CreateScreeningDto(
                screening.getDate().plusMinutes(10),
                filmId,
                HALL_ID
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createScreeningDto)
                .headers(headers -> headers.setBasicAuth(crateUserDto.mail(), crateUserDto.password()))
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
        var crateUserDto = UserFixture.createCrateUserDto();
        userService.createAdmin(crateUserDto);
        var screening = addScreening();

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_ADMIN_ENDPOINT + "/" + screening.getId())
                .headers(headers -> headers.setBasicAuth(crateUserDto.mail(), crateUserDto.password()))
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(screeningRepository.getById(screening.getId())).isEmpty();
    }

    @Test
    void screenings_are_gotten() {
        //given
        var filmTitle = "Sample title";
        addFilm(filmTitle);
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
                .jsonPath("$.screenings[0].date").isEqualTo(screening.getDate().toString())
                .jsonPath("$.screenings[0].filmTitle").isEqualTo(filmTitle);
    }

    @Test
    void screenings_are_gotten_by_date() {
        //given
        addFilm();
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
                .jsonPath("$.screenings[*].date").isEqualTo(screeningWithRequiredDate.getDate().toString());
    }

    @Test
    void seats_are_gotten_by_screening_id() {
        //given
        var screeningId = 1L;
        addSeats(screeningId);

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SCREENINGS_PUBLIC_ENDPOINT + "/" + screeningId + "/seats")
                        .queryParam("screeningId", screeningId)
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

    private void addSeats(Long screeningId) {
        ScreeningSeatFixture
                .createSeats(screeningId)
                .forEach(screeningSeatRepository::add);
    }


    private Screening addScreening() {
        return screeningRepository.add(createScreening());
    }

    private void addHall() {
        hallService.createHall(createCreateHallDto());
    }

    private void addFilm(String title) {
        filmService.createFilm(FilmFixture.createCreateFilmDto(title));
    }

    private void addFilm() {
        filmService.createFilm(FilmFixture.createCreateFilmDto());
    }

    private Screening addScreening(LocalDate date) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime);
        return screeningRepository.add(screening);
    }
}
