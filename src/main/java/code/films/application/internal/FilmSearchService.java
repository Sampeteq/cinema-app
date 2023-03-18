package code.films.application.internal;

import code.films.application.dto.FilmSearchParams;
import code.films.application.internal.mappers.FilmMapper;
import code.films.domain.FilmRepository;
import code.films.application.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FilmSearchService {

    private final FilmRepository filmRepository;

    private final FilmMapper filmMapper;

    public List<FilmDto> searchFilmsBy(FilmSearchParams params) {
        return filmRepository
                .findBy(params)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
