package com.example.film;

import com.example.SpringTestsSpec;
import com.example.film.exception.WrongFilmYearException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.film.FilmTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmIT extends SpringTestsSpec {

    @Autowired
    private FilmFacade filmFacade;

    @Test
    void should_add_film() {
        var addedFilm = filmFacade.add(sampleAddFilmDTO());
        assertThat(
                filmFacade.read(addedFilm.id())
        ).isEqualTo(addedFilm);
    }

    @Test
    void should_throw_exception_when_filmYear_is_neither_previous_nor_current_nor_next_one() {
        assertThrows(
                WrongFilmYearException.class,
                () -> filmFacade.add(
                        sampleAddFilmDTOWithWrongFilmYear()
                )
        );
    }

    @Test
    void should_return_all_films() {
        var sampleFilms = addSampleDistinctFilms(filmFacade);
        assertThat(filmFacade.readAll()).isEqualTo(sampleFilms);
    }

    @Test
    void should_return_films_with_given_category() {
        var sampleCategory = addSampleDistinctFilms(filmFacade)
                .get(0)
                .category();
        assertThat(
                filmFacade.readAllByCategory(sampleCategory)
        ).allMatch(
                film -> film.category().equals(sampleCategory)
        );
    }
}
