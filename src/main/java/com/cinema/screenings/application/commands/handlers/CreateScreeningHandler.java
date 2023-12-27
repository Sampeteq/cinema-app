package com.cinema.screenings.application.commands.handlers;

import com.cinema.films.application.queries.GetFilm;
import com.cinema.films.application.queries.handlers.GetFilmHandler;
import com.cinema.halls.application.queries.GetFirstAvailableHall;
import com.cinema.halls.application.queries.handlers.GetFirstAvailableHallHandler;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningDatePolicy;
import com.cinema.screenings.domain.ScreeningEndDateCalculator;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningSeat;
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
        var screeningSeats = hallDto
                .seats()
                .stream()
                .map(seatDto -> new ScreeningSeat(seatDto.rowNumber(), seatDto.number(), true))
                .toList();
        var screening = new Screening(
                command.date(),
                endDate,
                command.filmId(),
                hallDto.id(),
                screeningSeats
        );
        var addedScreening = screeningRepository.add(screening);
        log.info("Screening added:{}", addedScreening);
    }
}
