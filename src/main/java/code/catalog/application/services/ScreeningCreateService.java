package code.catalog.application.services;

import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.Screening;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.services.ScreeningDateValidateService;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ScreeningCreateService {

    private final ScreeningDateValidateService screeningDateValidateService;
    private final Clock clock;
    private final FilmRepository filmRepository;
    private final RoomAvailableService roomAvailableService;

    public void createScreening(ScreeningCreateDto dto) {
        screeningDateValidateService.validate(dto.date(), clock);
        var film = getFilmOrThrow(dto.filmId());
        var screeningDate = dto.date();
        var screeningEndDate = film.calculateScreeningEndDate(screeningDate);
        var availableRoom = roomAvailableService.getFirstAvailableRoom(
                screeningDate,
                screeningEndDate
        );
        var newScreening = Screening.create(screeningDate, film, availableRoom);
        film.addScreening(newScreening);
        filmRepository.add(film);
    }

    private Film getFilmOrThrow(Long filmId) {
        return filmRepository
                .readById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }
}
