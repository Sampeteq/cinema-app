package code.films;

import code.films.dto.FilmCreatingRequest;
import code.films.exception.FilmYearException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
class FilmFactory {

    private final FilmYearSpecification filmYearSpecification;

    private final FilmRepository filmRepository;

    Film createFilm(FilmCreatingRequest dto) {
        if (filmYearSpecification.isSatisfyBy(dto.year())) {
            var film = new Film(
                    UUID.randomUUID(),
                    dto.title(),
                    FilmCategory.fromDTO(dto.filmCategory()),
                    dto.year(),
                    dto.durationInMinutes()
            );
            return filmRepository.save(film);
        } else {
            if (filmYearSpecification instanceof PreviousCurrentOrNextOneFilmYearSpecification) {
                throw new FilmYearException("A film year must be previous, current or next one");
            } else {
                throw new IllegalArgumentException("Unsupported film year specification");
            }
        }
    }
}
