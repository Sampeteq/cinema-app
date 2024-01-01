package com.cinema.screenings.domain;

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

    public Screening createScreening(LocalDateTime date, int filmDurationInMinutes, Long filmId, Long hallId) {
        screeningDatePolicy.checkScreeningDate(date);
        var endDate = screeningEndDateCalculator.calculateEndDate(date, filmDurationInMinutes);
        var collisions = screeningRepository.getScreeningCollisions(date, endDate, hallId);
        if (!collisions.isEmpty()) {
            throw new ScreeningsCollisionsException();
        }
        return new Screening(date, endDate, filmId, hallId);
    }
}
