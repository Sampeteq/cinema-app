package code.catalog.application.handlers;

import code.catalog.application.commands.FilmCreateCommand;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.domain.Film;
import code.catalog.infrastructure.db.FilmRepository;
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
