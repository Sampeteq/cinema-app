package code.films.application.handlers;

import code.films.application.commands.FilmCreateCommand;
import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.domain.Film;
import code.films.infrastructure.db.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FilmCreateHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmDto handle(FilmCreateCommand command) {
        var film = Film.create(
                        command.title(),
                        command.category(),
                        command.year(),
                        command.durationInMinutes()
        );
        var addedFilm = filmRepository.add(film);
        return filmMapper.mapToDto(addedFilm);
    }
}
