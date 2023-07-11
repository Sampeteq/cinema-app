package code.catalog.application.services;

import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.Room;
import code.catalog.domain.Screening;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.services.ScreeningDateValidateService;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

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
        var availableRoom = getAvailableRoomOrThrow(screeningDate, screeningEndDate);
        var newScreening = Screening.create(screeningDate, film, availableRoom);
        film.addScreening(newScreening);
        filmRepository.add(film);
    }

    private Film getFilmOrThrow(Long filmId) {
        return filmRepository
                .readById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    private Room getAvailableRoomOrThrow(LocalDateTime screeningDate, LocalDateTime screeningEndDate) {
        return roomAvailableService.getFirstAvailableRoom(
                screeningDate,
                screeningEndDate
        ).orElseThrow(RoomsNoAvailableException::new);
    }
}
