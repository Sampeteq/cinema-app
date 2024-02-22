package com.cinema.screenings;

import com.cinema.films.FilmService;
import com.cinema.halls.HallRepository;
import com.cinema.halls.exceptions.HallNotFoundException;
import com.cinema.screenings.exceptions.ScreeningsCollisionsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScreeningFactory {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;
    private final FilmService filmService;
    private final HallRepository hallRepository;

    public void validateScreening(Screening screening) {
        screeningDatePolicy.checkScreeningDate(screening.getDate());
        if (!hallRepository.existsById(screening.getHallId())) {
            throw new HallNotFoundException();
        }
        var start = screening.getDate().toLocalDate().atStartOfDay();
        var end = start.plusHours(24).minusMinutes(1);
        var screenings = screeningRepository.findByHallIdAndDateBetween(
                screening.getHallId(),
                start,
                end
        );
        var film = filmService.getFilmByTitle(screening.getFilmTitle());
        var screeningEndDate = calculateEndDate(screening.getDate(), film.getDurationInMinutes());
        var isCollision = screenings
                .stream()
                .anyMatch(otherScreening -> collide(
                                screening.getDate(),
                                screeningEndDate,
                                otherScreening.getDate(),
                                calculateEndDate(otherScreening.getDate(), film.getDurationInMinutes())
                        )
                );
        if (isCollision) {
            throw new ScreeningsCollisionsException();
        }
    }

    private static LocalDateTime calculateEndDate(LocalDateTime date, int filmDurationInMinutes) {
        return date.plusMinutes(filmDurationInMinutes);
    }

    private static boolean collide(
            LocalDateTime date,
            LocalDateTime endDate,
            LocalDateTime otherDate,
            LocalDateTime otherEndDate
    ) {
        return
                (!otherDate.isAfter(date) && !otherEndDate.isBefore(date)) ||
                (otherDate.isAfter(date) && !otherDate.isAfter(endDate));
    }
}
