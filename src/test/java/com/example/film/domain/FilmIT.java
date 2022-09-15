package com.example.film.domain;

import com.example.film.domain.exception.WrongFilmYearException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmIT extends FilmTestSpec {
    @Test
    void should_add_film() {
        var addedFilm = filmAPI.addFilm(sampleAddFilmDto());
        assertThat(filmAPI.readFilmById(addedFilm.filmId())).isEqualTo(addedFilm);
    }

    @Test
    void should_Throw_Exception_When_FilmYear_Is_Neither_Previous_Nor_Current_Nor_Next_One() {
        assertThrows(
                WrongFilmYearException.class,
                () -> filmAPI.addFilm(
                        sampleAddFilmDtoWithWrongFilmYear()
                )
        );
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
