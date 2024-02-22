package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CURRENT_DATE;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);

    public static final long HALL_ID = 1L;

    public static Screening createScreening(Film film) {
        return new Screening(
                SCREENING_DATE,
                film,
                HALL_ID
        );
    }

    public static Screening createScreening(Film film, Long hallId) {
        return new Screening(
                SCREENING_DATE,
                film,
                hallId
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Film film) {
        return new Screening(
                screeningDate,
                film,
                HALL_ID
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Film film, Long hallId) {
        return new Screening(
                screeningDate,
                film,
                hallId
        );
    }
}