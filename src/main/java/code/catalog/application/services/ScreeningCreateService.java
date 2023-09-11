package code.catalog.application.services;

import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.Room;
import code.catalog.domain.Screening;
import code.catalog.domain.events.ScreeningCreatedEvent;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.services.ScreeningDateValidateService;
import code.shared.events.EventPublisher;
import code.shared.exceptions.EntityNotFoundException;
import code.shared.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class ScreeningCreateService {

    private final ScreeningDateValidateService screeningDateValidateService;
    private final TimeProvider timeProvider;
    private final FilmRepository filmRepository;
    private final RoomAvailableService roomAvailableService;
    private final TransactionTemplate transactionTemplate;
    private final EventPublisher eventPublisher;

    public void createScreening(ScreeningCreateDto dto) {
        screeningDateValidateService.validate(dto.date(), timeProvider.getCurrentDate());
        var addedScreening = transactionTemplate.execute(status -> {
            var film = readFilm(dto.filmId());
            var endDate = film.calculateScreeningEndDate(dto.date());
            var availableRoom = findAvailableRoom(dto.date(), endDate);
            var newScreening = Screening.create(dto.date(), film, availableRoom);
            film.addScreening(newScreening);
            return newScreening;
        });
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                addedScreening.getId(),
                addedScreening.getDate(),
                addedScreening.getRoom().getRowsNumber(),
                addedScreening.getRoom().getRowSeatsNumber()
        );
        eventPublisher.publish(screeningCreatedEvent);
    }

    private Film readFilm(Long filmId) {
        return filmRepository
                .readById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    private Room findAvailableRoom(LocalDateTime screeningDate, LocalDateTime screeningEndDate) {
        return roomAvailableService.findAvailableRoom(
                screeningDate,
                screeningEndDate
        ).orElseThrow(RoomsNoAvailableException::new);
    }
}
