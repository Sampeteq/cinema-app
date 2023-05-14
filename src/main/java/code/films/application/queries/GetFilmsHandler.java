package code.films.application.queries;

import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.application.queries.GetFilmsQuery;
import code.films.domain.FilmRepository;
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
