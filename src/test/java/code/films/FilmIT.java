package code.films;

import code.SpringTestsSpec;
import code.films.exception.FilmYearException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import static code.films.FilmTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmIT extends SpringTestsSpec {

    @Autowired
    private FilmFacade filmFacade;

    @Test
    void should_add_film() {
        var addedFilm = filmFacade.add(sampleAddFilmDTO());
        Assertions.assertThat(
                filmFacade.read(addedFilm.id())
        ).isEqualTo(addedFilm);
    }

    @ParameterizedTest
    @MethodSource("code.films.FilmTestUtils#wrongFilmYears")
    void should_throw_exception_when_film_year_is_neither_previous_nor_current_nor_next_one(Integer wrongFilmYear) {
        assertThrows(
                FilmYearException.class,
                () -> filmFacade.add(
                        sampleAddFilmDTOWithWrongFilmYear(wrongFilmYear)
                )
        );
    }

    @Test
    void should_return_all_films() {
        var sampleFilms = addSampleFilms(filmFacade);
        Assertions.assertThat(filmFacade.readAll()).isEqualTo(sampleFilms);
    }

    @Test
    void should_return_films_with_given_category() {
        var sampleCategory = addSampleFilms(filmFacade)
                .get(0)
                .category();
        Assertions.assertThat(
                filmFacade.readByCategory(sampleCategory)
        ).allMatch(
                film -> film.category().equals(sampleCategory)
        );
    }
}
