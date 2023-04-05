package code.films.applications.services;

import code.films.applications.dto.CreateFilmDto;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.films.domain.exceptions.FilmYearException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.ArrayList;
import java.util.UUID;

@Component
@AllArgsConstructor
public class FilmCreateService {

    private static final int CURRENT_YEAR = Year.now().getValue();

    private final FilmRepository filmRepository;

    public Film createFilm(CreateFilmDto dto) {
        if (!isFilmYearCorrect(dto.year())) {
            throw new FilmYearException("A film year must be previous, current or next one");
        }
        var film = new Film(
                UUID.randomUUID(),
                dto.title(),
                dto.filmCategory(),
                dto.year(),
                dto.durationInMinutes(),
                new ArrayList<>()
        );
        return filmRepository.save(film);
    }

    private static boolean isFilmYearCorrect(Integer year) {
        return year == CURRENT_YEAR - 1 || year == CURRENT_YEAR || year == CURRENT_YEAR + 1;
    }
}
