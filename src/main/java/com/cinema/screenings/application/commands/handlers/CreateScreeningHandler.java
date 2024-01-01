package com.cinema.screenings.application.commands.handlers;

import com.cinema.films.application.queries.GetFilm;
import com.cinema.films.application.queries.handlers.GetFilmHandler;
import com.cinema.halls.application.queries.GetHall;
import com.cinema.halls.application.queries.handlers.GetHallHandler;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.domain.ScreeningFactory;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateScreeningHandler {

    private final ScreeningFactory screeningFactory;
    private final ScreeningRepository screeningRepository;
    private final ScreeningSeatRepository screeningSeatRepository;
    private final GetFilmHandler getFilmHandler;
    private final GetHallHandler getHallHandler;

    @Transactional
    public void handle(CreateScreening command) {
        log.info("Command:{}", command);
        var filmDto = getFilmHandler.handle(new GetFilm(command.filmId()));
        log.info("Gotten film:{}", filmDto);
        var hallDto = getHallHandler.handle(new GetHall(command.hallId()));
        log.info("Gotten hall:{}", hallDto);
        var screening = screeningFactory.createScreening(
                command.date(),
                filmDto.durationInMinutes(),
                command.filmId(),
                command.hallId()
        );
        var addedScreening = screeningRepository.add(screening);
        log.info("Screening added:{}", addedScreening);
        var screeningSeats = hallDto
                .seats()
                .stream()
                .map(seatDto -> new ScreeningSeat(seatDto.rowNumber(), seatDto.number(), true, screening.getId()))
                .toList();
        screeningSeats.forEach(screeningSeatRepository::add);
        log.info("Added seats");
    }
}
