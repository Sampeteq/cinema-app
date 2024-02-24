package com.cinema.screenings;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CURRENT_DATE;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);

    public static final long FILM_ID = 1L;

    public static final long HALL_ID = 1L;

    public static Screening createScreening() {
        return new Screening(
                SCREENING_DATE,
                FILM_ID,
                HALL_ID
        );
    }

    public static Screening createScreening(Long filmId, Long hallId) {
        return new Screening(
                SCREENING_DATE,
                filmId,
                hallId
        );
    }


    public static Screening createScreening(LocalDateTime screeningDate) {
        return new Screening(
                screeningDate,
                FILM_ID,
                HALL_ID
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Long hallId) {
        return new Screening(
                screeningDate,
                FILM_ID,
                hallId
        );
    }
}