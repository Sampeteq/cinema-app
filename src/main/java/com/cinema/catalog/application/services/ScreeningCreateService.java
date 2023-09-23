package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.Seat;
import com.cinema.catalog.domain.events.ScreeningCreatedEvent;
import com.cinema.catalog.domain.services.ScreeningDateValidateService;
import com.cinema.rooms.application.services.RoomFacade;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
class ScreeningCreateService {

    private final ScreeningDateValidateService screeningDateValidateService;
    private final Clock clock;
    private final FilmRepository filmRepository;
    private final RoomFacade roomFacade;
    private final EventPublisher eventPublisher;

    @Transactional
    public void createScreening(ScreeningCreateDto dto) {
        screeningDateValidateService.validate(dto.date(), clock);
        var film = readFilm(dto.filmId());
        var endDate = film.calculateScreeningEndDate(dto.date());
        var roomDto = roomFacade.findFirstAvailableRoom(dto.date(), endDate);
        var seats = createSeats(roomDto.rowsNumber(), roomDto.rowSeatsNumber());
        var newScreening = new Screening(
                dto.date(),
                endDate,
                film,
                roomDto.customId(),
                seats
        );
        film.addScreening(newScreening);
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                newScreening.getDate(),
                newScreening.getEndDate(),
                newScreening.getRoomCustomId()
        );
        eventPublisher.publish(screeningCreatedEvent);
    }

    private Film readFilm(Long filmId) {
        return filmRepository
                .readById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    private List<Seat> createSeats(int rowsQuantity, int seatsQuantityInOneRow) {
        var isFree = true;
        return IntStream
                .rangeClosed(1, rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> IntStream.rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> new Seat(rowNumber, seatNumber, isFree))
                )
                .toList();
    }
}
