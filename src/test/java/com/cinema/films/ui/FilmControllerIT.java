package com.cinema.films.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.application.dto.CreateFilmDto;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.cinema.films.FilmFixture.createFilm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;

class FilmControllerIT extends BaseIT {

    private static final String FILM_PUBLIC_ENDPOINT = "/public/films";
    private static final String FILM_ADMIN_ENDPOINT = "/admin/films";

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void film_is_created() {
        //given
        var user = addUser();
        var id = 1L;
        var title = "Some title";
        var category = FilmCategory.COMEDY;
        var year = 2023;
        var durationInMinutes = 100;
        var createFilmDto = new CreateFilmDto(
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
                .bodyValue(createFilmDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();
        //then
        spec
                .expectStatus().isCreated()
                .expectHeader().location("/films")
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.title").isEqualTo(createFilmDto.title())
                .jsonPath("$.category").isEqualTo(createFilmDto.category().name())
                .jsonPath("$.year").isEqualTo(createFilmDto.year())
                .jsonPath("$.durationInMinutes").isEqualTo(createFilmDto.durationInMinutes());
    }

    @Test
    void film_title_is_unique() {
        //given
        var user = addUser();
        var film = filmRepository.add(createFilm());
        var createFilmDto = FilmFixture.createCreateFilmDto(film.getTitle());

        //when
        var spec = webTestClient
                .post()
                .uri(FILM_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFilmDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
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
    @WithMockUser(authorities = "ADMIN")
    void film_is_deleted() {
        //given
        var user = addUser();
        var film = filmRepository.add(createFilm());

        //when
        var spec = webTestClient
                .delete()
                .uri(FILM_ADMIN_ENDPOINT + "/" + film.getId())
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(filmRepository.getByTitle(film.getTitle())).isEmpty();
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
                        .path(FILM_PUBLIC_ENDPOINT + "/" + title)
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

    private User addUser() {
        return userRepository.add(UserFixture.createUser(UserRole.ADMIN));
    }
}
