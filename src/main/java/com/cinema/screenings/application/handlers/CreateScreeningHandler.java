package com.cinema.screenings.application.handlers;

import com.cinema.films.application.handlers.ReadFilmDurationInMinutesHandler;
import com.cinema.films.application.queries.ReadFilmDurationInMinutes;
import com.cinema.rooms.application.services.RoomService;
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
    private final ReadFilmDurationInMinutesHandler readFilmDurationInMinutesHandler;
    private final RoomService roomService;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CreateScreening command) {
        log.info("Command:{}", command);
        screeningDatePolicy.checkScreeningDate(command.date());
        var readFilmDurationInMinutesCommand = new ReadFilmDurationInMinutes(command.filmId());
        var filmDurationInMinutes = readFilmDurationInMinutesHandler.handle(readFilmDurationInMinutesCommand);
        log.info("Film duration in minutes:{}", filmDurationInMinutes);
        var endDate = command.date().plusMinutes(filmDurationInMinutes);
        log.info("Screening end date:{}", endDate);
        var roomDto = roomService.findFirstAvailableRoom(command.date(), endDate);
        log.info("Found room:{}", roomDto);
        var seats = createSeats(roomDto.rowsNumber(), roomDto.rowSeatsNumber());
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
                .flatMap(rowNumber -> IntStream.rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> new Seat(rowNumber, seatNumber, SeatStatus.FREE))
                )
                .toList();
    }
}
