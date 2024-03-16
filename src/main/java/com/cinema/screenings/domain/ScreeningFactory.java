package com.cinema.screenings.domain;

import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.HallService;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import lombok.RequiredArgsConstructor;

import java.time.Clock;

@RequiredArgsConstructor
public class ScreeningFactory {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;
    private final FilmService filmService;
    private final HallService hallService;
    private final Clock clock;

    Screening createScreening(ScreeningCreateDto screeningCreateDto) {
        screeningDatePolicy.checkScreeningDate(screeningCreateDto.date(), clock);
        var hall = hallService.getHallById(screeningCreateDto.hallId());
        var film = filmService.getFilmById(screeningCreateDto.filmId());
        var screeningEndDate = screeningCreateDto.date().plusMinutes(film.getDurationInMinutes());
        var collisions = screeningRepository.getCollisions(
                screeningCreateDto.date(),
                screeningEndDate,
                screeningCreateDto.hallId()
        );
        if (!collisions.isEmpty()) {
            throw new ScreeningsCollisionsException();
        }
        return new Screening(
                screeningCreateDto.date(),
                screeningEndDate,
                film,
                hall
        );
    }
}
