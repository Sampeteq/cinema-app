package code.films.client.commands.handlers;

import code.films.client.commands.CreateFilmCommand;
import code.films.client.dto.FilmDto;
import code.films.client.dto.mappers.FilmMapper;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateFilmHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmDto handle(CreateFilmCommand command) {
        var film = Film.create(
                Film.builder()
                        .title(command.title())
                        .category(command.filmCategory())
                        .year(command.year())
                        .durationInMinutes(command.durationInMinutes())
        );
        var addedFilm = filmRepository.add(film);
        return filmMapper.mapToDto(addedFilm);
    }
}
