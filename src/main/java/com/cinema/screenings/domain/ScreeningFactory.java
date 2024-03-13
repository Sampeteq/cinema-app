package com.cinema.screenings.domain;

import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.HallService;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
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
        if (!hallService.hallExistsById(screeningCreateDto.hallId())) {
            throw new HallNotFoundException();
        }
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
                screeningCreateDto.filmId(),
                screeningCreateDto.hallId()
        );
    }
}
