package com.example.film.domain;

import com.example.film.domain.exception.WrongFilmYearException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmIT extends FilmTestSpec {
    @Test
    void should_add_film() {
        var addedFilm = filmFacade.addFilm(sampleAddFilmDto());
        assertThat(
                filmFacade.readFilmById(addedFilm.id())
        ).isEqualTo(addedFilm);
    }

    @Test
    void should_throw_exception_when_filmYear_is_neither_previous_nor_current_nor_next_one() {
        assertThrows(
                WrongFilmYearException.class,
                () -> filmFacade.addFilm(
                        sampleAddFilmDtoWithWrongFilmYear()
                )
        );
    }

    @Test
    void should_return_all_films() {
        var sampleFilms = addSampleFilms();
        assertThat(filmFacade.readAllFilms()).isEqualTo(sampleFilms);
    }

    @Test
    void should_return_films_with_given_category() {
        var sampleCategory = addSampleFilms()
                .get(0)
                .category();
        assertThat(
                filmFacade.readFilmsByCategory(sampleCategory)
        ).allMatch(
                film -> film.category().equals(sampleCategory)
        );
    }
}
