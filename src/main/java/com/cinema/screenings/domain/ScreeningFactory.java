package com.cinema.screenings.domain;

import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.HallService;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ScreeningFactory {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;
    private final FilmService filmService;
    private final HallService hallService;
    private final Clock clock;

    void validateScreening(Screening screening) {
        screeningDatePolicy.checkScreeningDate(screening.getDate(), clock);
        if (!hallService.hallExistsById(screening.getHallId())) {
            throw new HallNotFoundException();
        }
        var film = filmService.getFilmById(screening.getFilmId());
        var screeningEndDate = calculateEndDate(screening.getDate(), film.getDurationInMinutes());
        var collisions = screeningRepository.getCollisions(
                screening.getDate(),
                screeningEndDate,
                screening.getHallId()
        );
        if (!collisions.isEmpty()) {
            throw new ScreeningsCollisionsException();
        }
        screening.assignEndDate(screeningEndDate);
    }

    private static LocalDateTime calculateEndDate(LocalDateTime date, int filmDurationInMinutes) {
        return date.plusMinutes(filmDurationInMinutes);
    }
}
