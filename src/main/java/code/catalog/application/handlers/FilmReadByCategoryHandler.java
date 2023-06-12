package code.catalog.application.handlers;

import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.domain.FilmCategory;
import code.catalog.infrastructure.db.FilmRepository;
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
