package com.cinema.screenings.ui.rest;

import com.cinema.SpringIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.rooms.application.commands.handlers.CreateRoomHandler;
import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cinema.screenings.ScreeningFixture.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixture.createCreateFilmCommand;
import static com.cinema.screenings.ScreeningFixture.createCreateRoomCommand;
import static com.cinema.screenings.ScreeningFixture.createScreening;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class ScreeningControllerIT extends SpringIT {

    private static final String SCREENINGS_BASE_ENDPOINT = "/screenings";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "12345";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private CreateFilmHandler createFilmHandler;

    @Autowired
    private CreateRoomHandler createRoomHandler;

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Autowired
    private CreateUserHandler createUserHandler;

    @Test
    void screening_is_created_only_by_admin() {
        //given
        addCommonUser();

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    void screening_is_created() {
        //given
        var filmId = 1L;
        var filmTitle = "Sample title";
        var roomId = "1";
        addAdminUser();
        addFilm(filmTitle);
        addRoom();
        var command = new CreateScreening(SCREENING_DATE, filmId);

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        var expectedDto = List.of(
                new ScreeningDto(
                        1L,
                        command.date(),
                        filmTitle,
                        roomId
                )
        );
        webTestClient
                .get()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .exchange()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(expectedDto.get(0).id())
                .jsonPath("$[0].date").isEqualTo(expectedDto.get(0).date().toString())
                .jsonPath("$[0].filmTitle").isEqualTo(expectedDto.get(0).filmTitle());
    }

    @Test
    void screening_and_current_date_difference_is_min_7_days() {
        //given
        var filmId = 1L;
        var filmTitle = "Sample title";
        addAdminUser();
        addFilm(filmTitle);
        addRoom();
        var screeningDate = LocalDateTime.now().plusDays(6);
        var command = new CreateScreening(screeningDate, filmId);

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
        var filmId = 1L;
        var filmTitle = "Sample film";
        addAdminUser();
        addRoom();
        addFilm(filmTitle);
        var screeningDate = LocalDateTime.now().plusDays(23);
        var command = new CreateScreening(screeningDate, filmId);

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
        var filmTitle = "Sample title";
        addAdminUser();
        addFilm(filmTitle);
        var screening = addScreening();
        var command = new CreateScreening(
                screening.getDate().plusMinutes(10),
                filmId
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        var expectedMessage = new RoomsNoAvailableException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void screening_is_deleted_only_by_admin() {
        //given
        addCommonUser();
        var screeningId = 1L;

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screeningId)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    void screening_is_deleted() {
        //given
        addAdminUser();
        var screening = addScreening();

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screening.getId())
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
                .uri(SCREENINGS_BASE_ENDPOINT)
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(hasSize(1))
                .jsonPath("$[*].*").value(everyItem(notNullValue()))
                .jsonPath("$[0].date").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].filmTitle").isEqualTo(filmTitle);
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
                        .path(SCREENINGS_BASE_ENDPOINT)
                        .queryParam("date", requiredDate.toString())
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.[*].date").isEqualTo(screeningWithRequiredDate.getDate().toString());
    }


    private Screening addScreening() {
        var screening = createScreening(SCREENING_DATE);
        return screeningRepository.add(screening);
    }

    private void addRoom() {
        createRoomHandler.handle(createCreateRoomCommand());
    }

    private void addFilm(String title) {
        createFilmHandler.handle(createCreateFilmCommand(title));
    }

    private void addCommonUser() {
        var command = new CreateUser(
                USERNAME,
                PASSWORD
        );
        createUserHandler.handle(command);
    }

    private void addAdminUser() {
        var command = new CreateAdmin(
                USERNAME,
                PASSWORD
        );
        createAdminHandler.handle(command);
    }

    private void addFilm() {
        createFilmHandler.handle(createCreateFilmCommand());
    }

    private Screening addScreening(LocalDate date) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime);
        return screeningRepository.add(screening);
    }
}
