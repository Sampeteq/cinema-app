package code.films.client.commands.handlers;

import code.films.client.commands.CreateFilmCommand;
import code.films.client.dto.mappers.FilmMapper;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.films.domain.exceptions.WrongFilmYearException;
import code.films.client.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.ArrayList;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CreateFilmHandler {

    private static final int CURRENT_YEAR = Year.now().getValue();
    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmDto handle(CreateFilmCommand command) {
        if (!isFilmYearCorrect(command.year())) {
            throw new WrongFilmYearException();
        }
        var film = new Film(
                UUID.randomUUID(),
                command.title(),
                command.filmCategory(),
                command.year(),
                command.durationInMinutes(),
                new ArrayList<>()
        );
        var addedFilm = filmRepository.add(film);
        return filmMapper.mapToDto(addedFilm);
    }

    private static boolean isFilmYearCorrect(Integer year) {
        return year == CURRENT_YEAR - 1 || year == CURRENT_YEAR || year == CURRENT_YEAR + 1;
    }
}
