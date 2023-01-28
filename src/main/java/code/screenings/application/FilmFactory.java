package code.screenings.application;

import code.screenings.domain.*;
import code.screenings.domain.dto.CreateFilmDto;
import code.screenings.domain.exception.FilmYearException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class FilmFactory {

    private final FilmYearSpecification filmYearSpecification;

    private final FilmRepository filmRepository;

    public Film createFilm(CreateFilmDto dto) {
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
