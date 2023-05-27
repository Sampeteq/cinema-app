package code.films.application.commands;

import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FilmCreationService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmDto createFilm(FilmCreationCommand command) {
        var film = Film.create(
                        command.title(),
                        command.filmCategory(),
                        command.year(),
                        command.durationInMinutes()
        );
        var addedFilm = filmRepository.add(film);
        return filmMapper.mapToDto(addedFilm);
    }
}
