package com.cinema.films.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.cinema.films.FilmFixture.createFilm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

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
        var id = 1L;
        var title = "Some title";
        var category = Film.Category.COMEDY;
        var year = 2023;
        var durationInMinutes = 100;
        var film = new Film(
                title,
                category,
                year,
                durationInMinutes
        );
        var user = addUser();
        //when
        var spec = webTestClient
                .post()
                .uri(FILM_ADMIN_ENDPOINT)
                .bodyValue(film)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();
        //then
        spec
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.title").isEqualTo(film.getTitle())
                .jsonPath("$.category").isEqualTo(film.getCategory().name())
                .jsonPath("$.year").isEqualTo(film.getYear())
                .jsonPath("$.durationInMinutes").isEqualTo(film.getDurationInMinutes());
    }

    @Test
    void film_title_is_unique() {
        //given
        var film = filmRepository.save(createFilm());
        var filmWithSameTitle = FilmFixture.createFilm(film.getTitle());
        var user = addUser();

        //when
        var spec = webTestClient
                .post()
                .uri(FILM_ADMIN_ENDPOINT)
                .bodyValue(filmWithSameTitle)
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
    void film_is_deleted() {
        //given
        var film = filmRepository.save(createFilm());
        var user = addUser();

        //when
        var spec = webTestClient
                .delete()
                .uri(FILM_ADMIN_ENDPOINT + "/" + film.getId())
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(filmRepository.findByTitle(film.getTitle())).isEmpty();
    }

    @Test
    void films_are_gotten() {
        //given
        var film = filmRepository.save(createFilm());

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
                .jsonPath("$[0].title").isEqualTo(film.getTitle())
                .jsonPath("$[0].category").isEqualTo(film.getCategory().name())
                .jsonPath("$[0].year").isEqualTo(film.getYear())
                .jsonPath("$[0].durationInMinutes").isEqualTo(film.getDurationInMinutes());
    }

    @Test
    void films_are_gotten_by_title() {
        //given
        var title = "Film";
        var otherTitle = "Other Film";
        filmRepository.save(createFilm(title));
        filmRepository.save(createFilm(otherTitle));

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
        var category = Film.Category.COMEDY;
        var otherCategory = Film.Category.DRAMA;
        filmRepository.save(createFilm(category));
        filmRepository.save(createFilm(otherCategory));

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
        return userRepository.save(UserFixture.createUser(User.Role.ADMIN));
    }
}
