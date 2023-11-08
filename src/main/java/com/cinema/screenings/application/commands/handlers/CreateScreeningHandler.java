package com.cinema.screenings.application.commands.handlers;

import com.cinema.films.application.queries.handlers.GetFilmHandler;
import com.cinema.films.application.queries.GetFilm;
import com.cinema.rooms.application.queries.handlers.GetFirstAvailableRoomHandler;
import com.cinema.rooms.application.queries.GetFirstAvailableRoom;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.Seat;
import com.cinema.screenings.domain.SeatStatus;
import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import com.cinema.screenings.domain.policies.ScreeningDatePolicy;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateScreeningHandler {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;
    private final GetFilmHandler getFilmHandler;
    private final GetFirstAvailableRoomHandler getFirstAvailableRoomHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CreateScreening command) {
        log.info("Command:{}", command);
        screeningDatePolicy.checkScreeningDate(command.date());
        var filmDto = getFilmHandler.handle(
                new GetFilm(command.filmId())
        );
        log.info("Film:{}", filmDto);
        var endDate = command.date().plusMinutes(filmDto.durationInMinutes());
        log.info("Screening end date:{}", endDate);
        var roomDto = getFirstAvailableRoomHandler.handle(
                new GetFirstAvailableRoom(command.date(), endDate)
        );
        log.info("Found room:{}", roomDto);
        var seats = createSeats(roomDto.rowsNumber(), roomDto.seatsNumberInOneRow());
        log.info("Created seats number:{}", seats.size());
        var screening = new Screening(
                command.date(),
                command.filmId(),
                roomDto.id(),
                seats
        );
        var addedScreening = screeningRepository.add(screening);
        log.info("Screening added:{}", addedScreening);
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                screening.getDate(),
                endDate,
                screening.getRoomId()
        );
        eventPublisher.publish(screeningCreatedEvent);
        log.info("Published event:{}", screeningCreatedEvent);
    }

    private List<Seat> createSeats(int rowsNumber, int seatsNumberInOneRow) {
        return IntStream
                .rangeClosed(1, rowsNumber)
                .boxed()
                .flatMap(rowNumber -> IntStream
                        .rangeClosed(1, seatsNumberInOneRow)
                        .mapToObj(seatNumber -> new Seat(rowNumber, seatNumber, SeatStatus.FREE))
                )
                .toList();
    }
}
