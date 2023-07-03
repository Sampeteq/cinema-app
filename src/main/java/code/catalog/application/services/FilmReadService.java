package code.catalog.application.services;

import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.ports.FilmRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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

    public FilmDto readByTitle(String title) {
        return filmRepository
                .readByTitle(title)
                .map(filmMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    public List<FilmDto> readByDate(LocalDate date) {
        return filmRepository
                .readByDate(date)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
