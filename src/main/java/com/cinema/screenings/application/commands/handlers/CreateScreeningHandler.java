package com.cinema.screenings.application.commands.handlers;

import com.cinema.films.application.queries.GetFilm;
import com.cinema.films.application.queries.handlers.GetFilmHandler;
import com.cinema.halls.application.queries.GetFirstAvailableHall;
import com.cinema.halls.application.queries.handlers.GetFirstAvailableHallHandler;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningEndDateCalculator;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import com.cinema.screenings.domain.ScreeningDatePolicy;
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
    private final ScreeningEndDateCalculator screeningEndDateCalculator;
    private final ScreeningRepository screeningRepository;
    private final GetFilmHandler getFilmHandler;
    private final GetFirstAvailableHallHandler getFirstAvailableHallHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CreateScreening command) {
        log.info("Command:{}", command);
        screeningDatePolicy.checkScreeningDate(command.date());
        var filmDto = getFilmHandler.handle(new GetFilm(command.filmId()));
        log.info("Gotten film:{}", filmDto);
        var endDate = screeningEndDateCalculator.calculateEndDate(
                command.date(),
                filmDto.durationInMinutes()
        );
        log.info("Screening end date:{}", endDate);
        var hallDto = getFirstAvailableHallHandler.handle(new GetFirstAvailableHall(command.date(), endDate));
        log.info("Gotten hall:{}", hallDto);
        var screening = new Screening(
                command.date(),
                command.filmId(),
                hallDto.id()
        );
        var addedScreening = screeningRepository.add(screening);
        log.info("Screening added:{}", addedScreening);
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                screening.getId(),
                screening.getDate(),
                endDate,
                hallDto
        );
        eventPublisher.publish(screeningCreatedEvent);
        log.info("Published event:{}", screeningCreatedEvent);
    }
}
