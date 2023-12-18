package com.cinema.films.ui;

import com.cinema.BaseIT;
import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.cinema.films.FilmFixture.createCreateFilmCommand;
import static com.cinema.films.FilmFixture.createFilm;
import static com.cinema.users.UserFixture.createCrateAdminCommand;
import static com.cinema.users.UserFixture.createCrateUserCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerIT extends BaseIT {

    private static final String FILM_PUBLIC_ENDPOINT = "/public/films";
    private static final String FILM_ADMIN_ENDPOINT = "/admin/films";

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void film_can_be_created_only_by_admin() {
        //given
        var crateUserCommand = createCrateUserCommand();
        createUserHandler.handle(crateUserCommand);

        //when
        var spec = webTestClient
                .post()
                .uri(FILM_ADMIN_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(crateUserCommand.mail(), crateUserCommand.password()))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    void film_is_created() {
        //given
        var crateAdminCommand = createCrateAdminCommand();
        createAdminHandler.handle(crateAdminCommand);
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
                .uri(FILM_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(crateAdminCommand.adminMail(), crateAdminCommand.adminPassword()))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(filmRepository.getById(id))
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
        var crateAdminCommand = createCrateAdminCommand();
        createAdminHandler.handle(crateAdminCommand);
        var film = filmRepository.add(createFilm());
        var command = createCreateFilmCommand(film.getTitle());

        //when
        var spec = webTestClient
                .post()
                .uri(FILM_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .headers(headers -> headers.setBasicAuth(crateAdminCommand.adminMail(), crateAdminCommand.adminPassword()))
                .exchange();

        //then

        var expectedMessage = new FilmTitleNotUniqueException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void film_is_deleted_only_by_admin() {
        //given
        var crateUserCommand = createCrateUserCommand();
        createUserHandler.handle(crateUserCommand);

        //when
        var spec = webTestClient
                .delete()
                .uri(FILM_ADMIN_ENDPOINT + "/1")
                .headers(headers -> headers.setBasicAuth(crateUserCommand.mail(), crateUserCommand.password()))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void film_is_deleted() {
        //given
        var crateAdminCommand = createCrateAdminCommand();
        createAdminHandler.handle(crateAdminCommand);
        var film = filmRepository.add(createFilm());

        //when
        var spec = webTestClient
                .delete()
                .uri(FILM_ADMIN_ENDPOINT + "/" + film.getId())
                .headers(headers -> headers.setBasicAuth(crateAdminCommand.adminMail(), crateAdminCommand.adminPassword()))
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(filmRepository.existsByTitle(film.getTitle())).isFalse();
    }

    @Test
    void films_are_gotten() {
        //given
        var film = filmRepository.add(createFilm());

        //when
        var spec = webTestClient
                .get()
                .uri(FILM_PUBLIC_ENDPOINT)
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(hasSize(1))
                .jsonPath("$.films[0].title").isEqualTo(film.getTitle())
                .jsonPath("$.films[0].category").isEqualTo(film.getCategory().name())
                .jsonPath("$.films[0].year").isEqualTo(film.getYear())
                .jsonPath("$.films[0].durationInMinutes").isEqualTo(film.getDurationInMinutes());
    }

    @Test
    void films_are_gotten_by_title() {
        //given
        var title = "Film";
        var otherTitle = "Other Film";
        filmRepository.add(createFilm(title));
        filmRepository.add(createFilm(otherTitle));

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(FILM_PUBLIC_ENDPOINT)
                        .queryParam("title", title)
                        .build()
                )
                .exchange();

        //then
        spec
                .expectBody()
                .jsonPath("$.*.title").value(everyItem(equalTo(title)));
    }

    @Test
    void films_are_gotten_by_category() {
        //given
        var category = FilmCategory.COMEDY;
        var otherCategory = FilmCategory.DRAMA;
        filmRepository.add(createFilm(category));
        filmRepository.add(createFilm(otherCategory));

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(FILM_PUBLIC_ENDPOINT)
                        .queryParam("category", category)
                        .build()
                )
                .exchange();

        //then
        spec
                .expectBody()
                .jsonPath("$.*.category").value(everyItem(equalTo(category.name())));
    }
}
