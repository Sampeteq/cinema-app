package code.catalog.application.services;

import code.catalog.application.commands.FilmCreateCommand;
import code.catalog.domain.Film;
import code.catalog.infrastructure.db.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmCreateService {

    private final FilmRepository filmRepository;

    public Long creteFilm(FilmCreateCommand command) {
        var film = Film.create(
                        command.title(),
                        command.category(),
                        command.year(),
                        command.durationInMinutes()
        );
        return filmRepository
                .add(film)
                .getId();
    }
}
