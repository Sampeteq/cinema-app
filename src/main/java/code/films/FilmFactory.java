package code.films;

import code.films.exception.FilmYearException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
class FilmFactory {

    private final FilmYearSpecification filmYearSpecification;

    Film createFilm(String title, FilmCategory category, int year) {
        if (filmYearSpecification.isSatisfyBy(year)) {
            return new Film(
                    UUID.randomUUID(),
                    title,
                    category,
                    year
            );
        } else {
            if (filmYearSpecification instanceof PreviousCurrentOrNextOneFilmYearSpecification) {
                throw new FilmYearException("A film year must be previous, current or next one");
            } else {
                throw new IllegalArgumentException("Unsupported film year specification");
            }
        }
    }
}
