package code.films.application.services;

import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.application.queries.FilmReadingQuery;
import code.films.domain.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FilmReadingService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmDto> read(FilmReadingQuery query) {
        return filmRepository
                .readBy(query)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
