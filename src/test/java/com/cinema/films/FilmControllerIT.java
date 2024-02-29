package com.cinema.films;

import com.cinema.BaseIT;
import com.cinema.users.User;
import com.cinema.users.UserFixtures;
import com.cinema.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private UserRepository userRepository;

    @Test
    void film_is_created() {
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

        webTestClient
                .post()
                .uri(FILM_ADMIN_ENDPOINT)
                .bodyValue(film)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.title").isEqualTo(film.getTitle())
                .jsonPath("$.category").isEqualTo(film.getCategory().name())
                .jsonPath("$.year").isEqualTo(film.getYear())
                .jsonPath("$.durationInMinutes").isEqualTo(film.getDurationInMinutes());
    }

    @Test
    void film_is_deleted() {
        var film = addFilm();
        var user = addUser();

        webTestClient
                .delete()
                .uri(FILM_ADMIN_ENDPOINT + "/" + film.getId())
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isNoContent();

        assertThat(filmRepository.findById(film.getId())).isEmpty();
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
        var category = Film.Category.COMEDY;
        var otherCategory = Film.Category.DRAMA;
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

    private void addFilm(Film.Category category) {
        filmRepository.save(createFilm(category));
    }

    private User addUser() {
        return userRepository.save(UserFixtures.createUser(User.Role.ADMIN));
    }
}
