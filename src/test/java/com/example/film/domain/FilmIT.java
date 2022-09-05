package com.example.film.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FilmIT extends FilmTestSpec {
    @Test
    void should_add_film() {
        var addedFilm = filmAPI.addFilm(sampleAddFilmDto());
        assertThat(filmAPI.readFilmById(addedFilm.filmId())).isEqualTo(addedFilm);
    }

    @Test
    void should_return_all_films() {
        var sampleFilms = addSampleFilms();
        assertThat(filmAPI.readAllFilms()).isEqualTo(sampleFilms);
    }

    @Test
    void should_return_films_with_given_category() {
        addSampleFilms();
        assertThat(
                filmAPI.readFilmsByCategory(FilmCategory.COMEDY)
        ).allMatch(
                film -> film.category().equals(FilmCategory.COMEDY)
        );
    }
}
