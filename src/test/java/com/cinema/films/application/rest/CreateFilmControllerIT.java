package com.cinema.films.application.rest;

import com.cinema.SpringIT;
import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.films.domain.exceptions.FilmYearOutOfRangeException;
import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.cinema.films.FilmFixture.createCreateFilmCommand;
import static com.cinema.films.FilmFixture.createFilm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateFilmControllerIT extends SpringIT {

    private static final String FILMS_BASE_ENDPOINT = "/films";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "12345";

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void film_can_be_created_only_by_admin() {
        //given
        addCommonUser();

        //when
        var spec = webTestClient
                .post()
                .uri(FILMS_BASE_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    void film_is_created() {
        //given
        addAdminUser();

        var id = 1L;
        var title = "Some filmId";
        var category = FilmCategory.COMEDY;
        var year = 2023;
        var durationInMinutes = 100;
        var command = new CreateFilm(
                title,
                category,
                year,
                durationInMinutes
        );

        //when
        var spec = webTestClient
                .post()
                .uri(FILMS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(filmRepository.readById(id))
                .isNotEmpty()
                .hasValueSatisfying(film -> {
                    assertEquals(command.title(), film.getTitle());
                    assertEquals(command.category(), film.getCategory());
                    assertEquals(command.year(), film.getYear());
                    assertEquals(command.durationInMinutes(), film.getDurationInMinutes());
                });
    }

    @Test
    void film_title_is_unique() {
        //given
        addAdminUser();
        var film = filmRepository.add(createFilm());
        var filmCreateDto = createCreateFilmCommand().withTitle(film.getTitle());

        //when
        var spec = webTestClient
                .post()
                .uri(FILMS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(filmCreateDto)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then

        var expectedMessage = new FilmTitleNotUniqueException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @ParameterizedTest
    @MethodSource("com.cinema.films.FilmFixture#getWrongFilmYears")
    void film_year_is_previous_current_or_nex_one(Integer wrongYear) {
        //given
        addAdminUser();
        var dto = createCreateFilmCommand().withYear(wrongYear);

        //when
        var spec = webTestClient
                .post()
                .uri(FILMS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        var expectedMessage = new FilmYearOutOfRangeException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
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
}
