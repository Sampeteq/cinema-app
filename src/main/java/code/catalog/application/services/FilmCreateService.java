package code.catalog.application.services;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.exceptions.FilmNotUniqueTitleException;
import code.catalog.domain.ports.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmCreateService {

    private final FilmRepository filmRepository;

    public void creteFilm(FilmCreateDto dto) {
        if (filmRepository.existsByTitle(dto.title())) {
            throw new FilmNotUniqueTitleException();
        }
        var film = Film.create(
                        dto.title(),
                        dto.category(),
                        dto.year(),
                        dto.durationInMinutes()
        );
        filmRepository.add(film);
    }
}
