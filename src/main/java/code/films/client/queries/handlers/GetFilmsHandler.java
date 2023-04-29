package code.films.client.queries.handlers;

import code.films.client.queries.GetFilmsQuery;
import code.films.domain.FilmRepository;
import code.films.client.dto.FilmDto;
import code.films.client.dto.mappers.FilmMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetFilmsHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmDto> handle(GetFilmsQuery query) {
        return filmRepository
                .readBy(query)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
