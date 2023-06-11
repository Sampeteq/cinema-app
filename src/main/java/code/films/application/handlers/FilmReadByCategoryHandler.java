package code.films.application.handlers;

import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.domain.FilmCategory;
import code.films.infrastructure.db.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmReadByCategoryHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmDto> handle(FilmCategory category) {
        return filmRepository
                .readByCategory(category)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
