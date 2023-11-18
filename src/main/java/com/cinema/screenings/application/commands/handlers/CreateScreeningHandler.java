package com.cinema.screenings.application.commands.handlers;

import com.cinema.films.application.FilmApi;
import com.cinema.rooms.application.RoomApi;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import com.cinema.screenings.domain.policies.ScreeningDatePolicy;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateScreeningHandler {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;
    private final FilmApi filmApi;
    private final RoomApi roomApi;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CreateScreening command) {
        log.info("Command:{}", command);
        screeningDatePolicy.checkScreeningDate(command.date());
        var filmDto = filmApi.getFilmById(command.filmId());
        log.info("Gotten film:{}", filmDto);
        var endDate = command.date().plusMinutes(filmDto.durationInMinutes());
        log.info("Screening end date:{}", endDate);
        var roomDto = roomApi.getFirstAvailableRoom(command.date(), endDate);
        log.info("Gotten room:{}", roomDto);
        var screening = new Screening(
                command.date(),
                command.filmId(),
                roomDto.id()
        );
        var addedScreening = screeningRepository.add(screening);
        log.info("Screening added:{}", addedScreening);
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                screening.getId(),
                screening.getDate(),
                endDate,
                roomDto
        );
        eventPublisher.publish(screeningCreatedEvent);
        log.info("Published event:{}", screeningCreatedEvent);
    }
}
