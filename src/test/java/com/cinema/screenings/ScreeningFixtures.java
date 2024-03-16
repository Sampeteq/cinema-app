package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningCreateDto;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CURRENT_DATE;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);

    public static final LocalDateTime SCREENING_END_DATE = SCREENING_DATE.plusHours(2);

    public static ScreeningCreateDto createScreeningCreateDto(Long filmId, Long hallId) {
        return new ScreeningCreateDto(
                SCREENING_DATE,
                filmId,
                hallId
        );
    }

    public static Screening createScreening(Film film, Hall hall) {
        return new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                film,
                hall
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Film film, Hall hall) {
        return new Screening(
                screeningDate,
                SCREENING_END_DATE,
                film,
                hall
        );
    }
}