package com.cinema.films.application.rest;

import com.cinema.SpringIT;
import com.cinema.films.application.dto.FilmCreateDto;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.films.domain.exceptions.FilmYearOutOfRangeException;
import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.cinema.films.FilmFixture.createFilm;
import static com.cinema.films.FilmFixture.createFilmCreateDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerIT extends SpringIT {

    private static final String FILMS_BASE_ENDPOINT = "/films";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "12345";

    @Autowired
    private UserService userService;

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
        var dto = new FilmCreateDto(
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
                .bodyValue(dto)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        assertThat(filmRepository.readById(id))
                .isNotEmpty()
                .hasValueSatisfying(film -> {
                    assertEquals(dto.title(), film.getTitle());
                    assertEquals(dto.category(), film.getCategory());
                    assertEquals(dto.year(), film.getYear());
                    assertEquals(dto.durationInMinutes(), film.getDurationInMinutes());
                });
    }

    @Test
    void film_title_is_unique() {
        //given
        addAdminUser();
        var film = filmRepository.add(createFilm());
        var filmCreateDto = createFilmCreateDto().withTitle(film.getTitle());

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
        var dto = createFilmCreateDto().withYear(wrongYear);

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

    @Test
    void film_is_deleted_only_by_admin() {
        //given
        addCommonUser();

        //when
        var spec = webTestClient
                .delete()
                .uri(FILMS_BASE_ENDPOINT + "/Film 1")
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void film_is_deleted() {
        //given
        addAdminUser();
        var film = filmRepository.add(createFilm());

        //when
        var spec = webTestClient
                .delete()
                .uri(FILMS_BASE_ENDPOINT + "/" + film.getId())
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(filmRepository.existsByTitle(film.getTitle())).isFalse();
    }

    @Test
    void films_are_read() {
        //given
        var film = filmRepository.add(createFilm());

        //when
        var spec = webTestClient
                .get()
                .uri(FILMS_BASE_ENDPOINT)
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
    void films_are_read_by_title() {
        //given
        var title = "Film";
        var otherTitle = "Other Film";
        filmRepository.add(createFilm(title));
        filmRepository.add(createFilm(otherTitle));

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(FILMS_BASE_ENDPOINT)
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
    void films_are_read_by_category() {
        //given
        var category = FilmCategory.COMEDY;
        var otherCategory = FilmCategory.DRAMA;
        filmRepository.add(createFilm(category));
        filmRepository.add(createFilm(otherCategory));

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(FILMS_BASE_ENDPOINT)
                        .queryParam("category", category)
                        .build()
                )
                .exchange();

        //then
        spec
                .expectBody()
                .jsonPath("$.*.category").value(everyItem(equalTo(category.name())));
    }

    private void addCommonUser() {
        var userCreateDto = new UserCreateDto(
                USERNAME,
                PASSWORD,
                PASSWORD
        );
        userService.createCommonUser(userCreateDto);
    }

    private void addAdminUser() {
        var userCreateDto = new UserCreateDto(
                USERNAME,
                PASSWORD,
                PASSWORD
        );
        userService.createAdmin(userCreateDto);
    }
}
