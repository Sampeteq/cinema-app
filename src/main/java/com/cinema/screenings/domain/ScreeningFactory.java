package com.cinema.screenings.domain;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScreeningFactory {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningEndDateCalculator screeningEndDateCalculator;
    private final ScreeningRepository screeningRepository;

    public Screening createScreening(LocalDateTime date, Film film, Hall hall) {
        screeningDatePolicy.checkScreeningDate(date);
        var endDate = screeningEndDateCalculator.calculateEndDate(date, film.getDurationInMinutes());
        var collisions = screeningRepository.getScreeningCollisions(date, endDate, hall.getId());
        if (!collisions.isEmpty()) {
            throw new ScreeningsCollisionsException();
        }
        var screening = new Screening(date, endDate, film, hall);
        var screeningSeats = hall
                .getSeats()
                .stream()
                .map(hallSeat -> new ScreeningSeat(true, hallSeat, screening))
                .toList();
        screening.assignSeats(screeningSeats);
        return screening;
    }
}
