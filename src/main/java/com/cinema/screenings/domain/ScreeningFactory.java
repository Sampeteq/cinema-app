package com.cinema.screenings.domain;

import com.cinema.films.domain.Film;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScreeningFactory {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;

    public Screening createScreening(LocalDateTime screeningDate, Film film, Long hallId) {
        screeningDatePolicy.checkScreeningDate(screeningDate);
        var start = screeningDate.toLocalDate().atStartOfDay();
        var end = start.plusHours(24).minusMinutes(1);
        var screenings = screeningRepository.findByHallIdAndDateBetween(hallId, start, end);
        var screeningEndDate = calculateEndDate(screeningDate, film.getDurationInMinutes());
        var isCollision = screenings
                .stream()
                .anyMatch(otherScreening -> collide(
                                screeningDate,
                                screeningEndDate,
                                otherScreening.getDate(),
                                calculateEndDate(otherScreening.getDate(), film.getDurationInMinutes())
                        )
                );
        if (isCollision) {
            throw new ScreeningsCollisionsException();
        }
        return new Screening(screeningDate, film, hallId);
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
