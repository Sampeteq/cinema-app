package code.catalog.application.services;

import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.domain.ports.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FilmReadService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    List<FilmDto> readAll() {
        return filmRepository
                .readAll()
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
