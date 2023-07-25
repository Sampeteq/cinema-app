package code.catalog.application.services;

import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.dto.ScreeningMapper;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.Screening;
import code.catalog.infrastructure.db.ScreeningReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScreeningReadService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> readAllScreenings() {
        return screeningReadOnlyRepository
                .findAll()
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<ScreeningDto> readScreeningsByFilmTitle(String filmTitle) {
        return screeningReadOnlyRepository
                .findByFilm_Title(filmTitle)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<ScreeningDto> readScreeningsByCategory(FilmCategory category) {
        return screeningReadOnlyRepository
                .findByFilm_Category(category)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<ScreeningDto> readScreeningsByDate(LocalDate date) {
        return screeningReadOnlyRepository
                .findByDateBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay())
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }
}
