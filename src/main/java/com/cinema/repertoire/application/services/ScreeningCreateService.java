package com.cinema.repertoire.application.services;

import com.cinema.repertoire.application.dto.ScreeningCreateDto;
import com.cinema.repertoire.domain.Film;
import com.cinema.repertoire.domain.FilmRepository;
import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.ScreeningRepository;
import com.cinema.repertoire.domain.Seat;
import com.cinema.repertoire.domain.SeatStatus;
import com.cinema.repertoire.domain.events.ScreeningCreatedEvent;
import com.cinema.repertoire.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.rooms.application.services.RoomFacade;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
class ScreeningCreateService {

    private final Clock clock;
    private final FilmRepository filmRepository;
    private final RoomFacade roomFacade;
    private final ScreeningRepository screeningRepository;
    private final EventPublisher eventPublisher;

    public void createScreening(ScreeningCreateDto dto) {
        if (isScreeningDateOutOfRange(dto.date())) {
            throw new ScreeningDateOutOfRangeException();
        }
        var film = readFilm(dto.filmTitle());
        var endDate = film.calculateScreeningEndDate(dto.date());
        var roomDto = roomFacade.findFirstAvailableRoom(dto.date(), endDate);
        var seats = createSeats(roomDto.rowsNumber(), roomDto.rowSeatsNumber());
        var screening = new Screening(
                dto.date(),
                film,
                roomDto.id(),
                seats
        );
        screeningRepository.add(screening);
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                screening.getDate(),
                endDate,
                screening.getRoomId()
        );
        eventPublisher.publish(screeningCreatedEvent);
    }

    private Film readFilm(String filmTitle) {
        return filmRepository
                .readByTitle(filmTitle)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    private boolean isScreeningDateOutOfRange(LocalDateTime screeningDate) {
        var currentDate = LocalDateTime.now(clock);
        var datesDifference = Duration
                .between(screeningDate, currentDate)
                .abs()
                .toDays();
        return datesDifference < 7 || datesDifference > 21;
    }

    private List<Seat> createSeats(int rowsQuantity, int seatsQuantityInOneRow) {
        return IntStream
                .rangeClosed(1, rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> IntStream.rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> new Seat(rowNumber, seatNumber, SeatStatus.FREE))
                )
                .toList();
    }
}
