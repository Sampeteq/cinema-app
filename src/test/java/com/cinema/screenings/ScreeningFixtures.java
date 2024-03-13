package com.cinema.screenings;

import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningCreateDto;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CURRENT_DATE;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);

    public static final LocalDateTime SCREENING_END_DATE = SCREENING_DATE.plusHours(2);

    public static final long FILM_ID = 1L;

    public static final long HALL_ID = 1L;

    public static ScreeningCreateDto createScreeningCreateDto(Long filmId, Long hallId) {
        return new ScreeningCreateDto(
                SCREENING_DATE,
                filmId,
                hallId
        );
    }

    public static Screening createScreening() {
        return new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                FILM_ID,
                HALL_ID
        );
    }

    public static Screening createScreening(Long filmId, Long hallId) {
        return new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                filmId,
                hallId
        );
    }


    public static Screening createScreening(LocalDateTime screeningDate) {
        return new Screening(
                screeningDate,
                SCREENING_END_DATE,
                FILM_ID,
                HALL_ID
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Long hallId) {
        return new Screening(
                screeningDate,
                SCREENING_END_DATE,
                FILM_ID,
                hallId
        );
    }
}