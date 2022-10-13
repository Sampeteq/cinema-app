package code.film;

import code.SpringTestsSpec;
import code.film.exception.FilmYearException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.film.FilmTestUtils.addSampleDistinctFilms;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmIT extends SpringTestsSpec {

    @Autowired
    private FilmFacade filmFacade;

    @Test
    void should_add_film() {
        var addedFilm = filmFacade.add(FilmTestUtils.sampleAddFilmDTO());
        Assertions.assertThat(
                filmFacade.read(addedFilm.id())
        ).isEqualTo(addedFilm);
    }

    @Test
    void should_throw_exception_when_filmYear_is_neither_previous_nor_current_nor_next_one() {
        assertThrows(
                FilmYearException.class,
                () -> filmFacade.add(
                        FilmTestUtils.sampleAddFilmDTOWithWrongFilmYear()
                )
        );
    }

    @Test
    void should_return_all_films() {
        var sampleFilms = addSampleDistinctFilms(filmFacade);
        Assertions.assertThat(filmFacade.readAll()).isEqualTo(sampleFilms);
    }

    @Test
    void should_return_films_with_given_category() {
        var sampleCategory = addSampleDistinctFilms(filmFacade)
                .get(0)
                .category();
        Assertions.assertThat(
                filmFacade.readAllByCategory(sampleCategory)
        ).allMatch(
                film -> film.category().equals(sampleCategory)
        );
    }
}
