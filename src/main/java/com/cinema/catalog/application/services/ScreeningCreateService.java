package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.Room;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.Seat;
import com.cinema.catalog.domain.exceptions.RoomsNoAvailableException;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.services.ScreeningDateValidateService;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.shared.time.TimeProvider;
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

    void createScreening(ScreeningCreateDto dto) {
        screeningDateValidateService.validate(dto.date(), timeProvider.getCurrentDate());
        transactionTemplate.execute(status -> {
            var film = readFilm(dto.filmId());
            var endDate = film.calculateScreeningEndDate(dto.date());
            var availableRoom = findAvailableRoom(dto.date(), endDate);
            var seats = createSeats(availableRoom.getRowsNumber(), availableRoom.getRowSeatsNumber());
            var newScreening = Screening.create(dto.date(), film, availableRoom, seats);
            film.addScreening(newScreening);
            return newScreening;
        });
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
