package code.films.domain.queries.handlers;

import code.films.domain.queries.SearchFilmsQuery;
import code.films.infrastructure.rest.mappers.FilmMapper;
import code.films.domain.FilmRepository;
import code.films.infrastructure.rest.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SearchFilmsQueryHandler {

    private final FilmRepository filmRepository;

    private final FilmMapper filmMapper;

    public List<FilmDto> searchFilmsBy(SearchFilmsQuery params) {
        return filmRepository
                .findBy(params)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
