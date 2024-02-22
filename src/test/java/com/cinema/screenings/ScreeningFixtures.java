package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CURRENT_DATE;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);

    public static final LocalDateTime TOO_EARLY_SCREENING_DATE = CURRENT_DATE.plusDays(6);

    public static final LocalDateTime TOO_LATE_SCREENING_DATE = CURRENT_DATE.plusDays(22);

    public static Screening createScreening(Film film, Hall hall) {
        return new Screening(
                SCREENING_DATE,
                film,
                hall
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Film film, Hall hall) {
        return new Screening(
                screeningDate,
                film,
                hall
        );
    }
}