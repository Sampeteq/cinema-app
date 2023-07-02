package code.catalog.application.services;

import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.Screening;
import code.catalog.domain.factories.SeatFactory;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.services.ScreeningDateValidateService;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ScreeningCreateService {

    private final ScreeningDateValidateService screeningDateValidateService;
    private final Clock clock;
    private final FilmRepository filmRepository;
    private final RoomAvailableService roomAvailableService;
    private final SeatFactory seatFactory;

    @Transactional
    public void createScreening(ScreeningCreateDto dto) {
        screeningDateValidateService.validate(dto.date(), clock);
        var film = getFilmOrThrow(dto.filmId());
        var screeningDate = dto.date();
        var screeningFinishDate = dto.date().plusMinutes(film.getDurationInMinutes());
        var availableRoom = roomAvailableService.getFirstAvailableRoom(
                screeningDate,
                screeningFinishDate
        );
        var seats = seatFactory.createSeats(availableRoom);
        var newScreening = Screening.create(dto.date(), film, availableRoom, seats);
        film.addScreening(newScreening);
    }

    private Film getFilmOrThrow(Long filmId) {
        return filmRepository
                .readById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }
}
