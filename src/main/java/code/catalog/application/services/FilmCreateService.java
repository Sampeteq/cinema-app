package code.catalog.application.services;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.domain.Film;
import code.catalog.infrastructure.db.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmCreateService {

    private final FilmRepository filmRepository;

    public Long creteFilm(FilmCreateDto dto) {
        var film = Film.create(
                        dto.title(),
                        dto.category(),
                        dto.year(),
                        dto.durationInMinutes()
        );
        return filmRepository
                .add(film)
                .getId();
    }
}
