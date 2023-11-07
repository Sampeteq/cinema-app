package com.cinema.screenings.application.commands.handlers;

import com.cinema.films.application.queries.handlers.ReadFilmHandler;
import com.cinema.films.application.queries.ReadFilm;
import com.cinema.rooms.application.queries.handlers.FindFirstAvailableRoomHandler;
import com.cinema.rooms.application.queries.FindFirstAvailableRoom;
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
    private final ReadFilmHandler readFilmHandler;
    private final FindFirstAvailableRoomHandler findFirstAvailableRoomHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CreateScreening command) {
        log.info("Command:{}", command);
        screeningDatePolicy.checkScreeningDate(command.date());
        var readFilmCommand = new ReadFilm(command.filmId());
        var filmDto = readFilmHandler.handle(readFilmCommand);
        log.info("Film:{}", filmDto);
        var endDate = command.date().plusMinutes(filmDto.durationInMinutes());
        log.info("Screening end date:{}", endDate);
        var findFirstAvailableRoomCommand = new FindFirstAvailableRoom(command.date(), endDate);
        var roomDto = findFirstAvailableRoomHandler.findFirstAvailableRoom(findFirstAvailableRoomCommand);
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

    private List<Seat> createSeats(int rowsQuantity, int seatsQuantityInOneRow) {
        return IntStream
                .rangeClosed(1, rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> IntStream
                        .rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> new Seat(rowNumber, seatNumber, SeatStatus.FREE))
                )
                .toList();
    }
}
