package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.Room;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.events.ScreeningCreatedEvent;
import com.cinema.catalog.domain.exceptions.RoomsNoAvailableException;
import com.cinema.catalog.domain.ports.FilmRepository;
import com.cinema.catalog.domain.services.ScreeningDateValidateService;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.shared.time.TimeProvider;
import com.cinema.catalog.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
class ScreeningCreateService {

    private final ScreeningDateValidateService screeningDateValidateService;
    private final TimeProvider timeProvider;
    private final TransactionTemplate transactionTemplate;
    private final FilmRepository filmRepository;
    private final RoomAvailableService roomAvailableService;
    private final EventPublisher eventPublisher;

    void createScreening(ScreeningCreateDto dto) {
        screeningDateValidateService.validate(dto.date(), timeProvider.getCurrentDate());
        var addedScreening = transactionTemplate.execute(status -> {
            var film = readFilm(dto.filmId());
            var endDate = film.calculateScreeningEndDate(dto.date());
            var availableRoom = findAvailableRoom(dto.date(), endDate);
            var seats = createSeats(availableRoom.getRowsNumber(), availableRoom.getRowSeatsNumber());
            var newScreening = Screening.create(dto.date(), film, availableRoom, seats);
            film.addScreening(newScreening);
            return newScreening;
        });
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                addedScreening.getId(),
                addedScreening.getDate(),
                addedScreening.getFilm().getTitle(),
                addedScreening.getRoom().getCustomId(),
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

    private List<Seat> createSeats(int rowsQuantity, int seatsQuantityInOneRow) {
        return IntStream
                .rangeClosed(1, rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> IntStream.rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> Seat.create(rowNumber, seatNumber))
                )
                .toList();
    }
}
