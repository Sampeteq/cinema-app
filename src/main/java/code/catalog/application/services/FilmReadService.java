package code.catalog.application.services;

import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.domain.FilmCategory;
import code.catalog.infrastructure.db.FilmRepository;
import code.shared.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
public class FilmReadService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmDto> readAll() {
        return filmRepository
                .readAll()
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }

    public List<FilmDto> readByCategory(FilmCategory category) {
        return filmRepository
                .readByCategory(category)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public FilmDto readByTitle(String title) {
        return filmRepository
                .readByTitle(title)
                .map(filmMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }
}
