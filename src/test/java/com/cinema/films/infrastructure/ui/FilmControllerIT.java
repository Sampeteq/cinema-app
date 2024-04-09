package com.cinema.films.infrastructure.ui;

import com.cinema.BaseIT;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.domain.FilmCreateDto;
import com.cinema.films.domain.FilmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static com.cinema.films.FilmFixtures.createFilm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;

class FilmControllerIT extends BaseIT {

    private static final String FILM_PUBLIC_ENDPOINT = "/public/films";

    private static final String FILM_ADMIN_ENDPOINT = "/admin/films";

    @Autowired
    private FilmRepository filmRepository;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void film_is_created() {
        var title = "Some title";
        var category = FilmCategory.COMEDY;
        var year = 2023;
        var durationInMinutes = 100;
        var film = new FilmCreateDto(
                title,
                category,
                year,
                durationInMinutes
        );

        webTestClient
                .post()
                .uri(FILM_ADMIN_ENDPOINT)
                .bodyValue(film)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.title").isEqualTo(film.title())
                .jsonPath("$.category").isEqualTo(film.category().name())
                .jsonPath("$.year").isEqualTo(film.year())
                .jsonPath("$.durationInMinutes").isEqualTo(film.durationInMinutes());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void film_is_deleted() {
        var film = addFilm();

        webTestClient
                .delete()
                .uri(FILM_ADMIN_ENDPOINT + "/" + film.getId())
                .exchange()
                .expectStatus()
                .isNoContent();

        assertThat(filmRepository.getById(film.getId())).isEmpty();
    }

    @Test
    void films_are_gotten() {
        var film = addFilm();

        webTestClient
                .get()
                .uri(FILM_PUBLIC_ENDPOINT)
                .exchange()
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
        var title = "Title 1";
        var otherTitle = "Title 2";
        addFilm(title);
        addFilm(otherTitle);

        webTestClient
                .get()
                .uri(FILM_PUBLIC_ENDPOINT + "/title/" + title)
                .exchange()
                .expectBody()
                .jsonPath("$.*.title").value(everyItem(equalTo(title)));
    }

    @Test
    void films_are_gotten_by_category() {
        var category = FilmCategory.COMEDY;
        var otherCategory = FilmCategory.DRAMA;
        addFilm(category);
        addFilm(otherCategory);

        webTestClient
                .get()
                .uri(FILM_PUBLIC_ENDPOINT + "/category/" + category)
                .exchange()
                .expectBody()
                .jsonPath("$.*.category").value(everyItem(equalTo(category.name())));
    }

    private Film addFilm() {
        return filmRepository.save(createFilm());
    }

    private void addFilm(String title) {
        filmRepository.save(createFilm(title));
    }

    private void addFilm(FilmCategory category) {
        filmRepository.save(createFilm(category));
    }
}
