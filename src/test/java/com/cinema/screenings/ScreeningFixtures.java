package com.cinema.screenings;

import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CURRENT_DATE;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);

    public static final LocalDateTime SCREENING_END_DATE = SCREENING_DATE.plusHours(2);

    public static final long FILM_ID = 1L;

    public static final long HALL_ID = 1L;

    public static Screening createScreening() {
        var screening = new Screening(
                SCREENING_DATE,
                FILM_ID,
                HALL_ID
        );
        screening.assignEndDate(SCREENING_END_DATE);
        return screening;
    }

    public static Screening createScreening(Long filmId, Long hallId) {
        var screening = new Screening(
                SCREENING_DATE,
                filmId,
                hallId
        );
        screening.assignEndDate(SCREENING_END_DATE);
        return screening;
    }


    public static Screening createScreening(LocalDateTime screeningDate) {
        var screening = new Screening(
                screeningDate,
                FILM_ID,
                HALL_ID
        );
        screening.assignEndDate(SCREENING_END_DATE);
        return screening;
    }

    public static Screening createScreening(LocalDateTime screeningDate, Long hallId) {
        var screening = new Screening(
                screeningDate,
                FILM_ID,
                hallId
        );
        screening.assignEndDate(SCREENING_END_DATE);
        return screening;
    }
}