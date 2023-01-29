package code.screenings.application;

import code.screenings.domain.Film;
import code.screenings.domain.FilmRepository;
import code.screenings.application.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FilmSearcher {

    private final FilmRepository filmRepository;

    public List<FilmDto> searchFilmsBy(FilmSearchParams params) {
        return filmRepository
                .findBy(params)
                .stream()
                .map(Film::toDto)
                .toList();
    }
}
