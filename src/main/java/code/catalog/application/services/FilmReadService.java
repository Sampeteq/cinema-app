package code.catalog.application.services;

import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.dto.ScreeningMapper;
import code.catalog.domain.Screening;
import code.catalog.domain.ports.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Comparator.comparing;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FilmReadService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final ScreeningMapper screeningMapper;

    public List<FilmDto> readAll() {
        return filmRepository
                .readAll()
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }

    public List<ScreeningDto> readAllScreenings() {
        return filmRepository
                .readAll()
                .stream()
                .flatMap(film -> film
                        .getScreenings()
                        .stream()
                        .sorted(comparing(Screening::getDate))
                        .map(screeningMapper::mapToDto)
                ).toList();
    }
}
