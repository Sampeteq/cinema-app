package com.cinema.screenings;

import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CURRENT_DATE;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);

    public static final String FILM_TITLE = "Film 1";

    public static final long HALL_ID = 1L;

    public static Screening createScreening() {
        return new Screening(
                SCREENING_DATE,
                FILM_TITLE,
                HALL_ID
        );
    }

    public static Screening createScreening(String filmTitle) {
        return new Screening(
                SCREENING_DATE,
                filmTitle,
                HALL_ID
        );
    }

    public static Screening createScreening(String filmTitle, Long hallId) {
        return new Screening(
                SCREENING_DATE,
                filmTitle,
                hallId
        );
    }


    public static Screening createScreening(Long hallId) {
        return new Screening(
                SCREENING_DATE,
                FILM_TITLE,
                hallId
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate) {
        return new Screening(
                screeningDate,
                FILM_TITLE,
                HALL_ID
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, String filmTitle) {
        return new Screening(
                screeningDate,
                filmTitle,
                HALL_ID
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Long hallId) {
        return new Screening(
                screeningDate,
                FILM_TITLE,
                hallId
        );
    }
}