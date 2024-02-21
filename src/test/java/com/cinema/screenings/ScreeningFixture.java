package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;

import static com.cinema.ClockFixtures.CLOCK;

public final class ScreeningFixture {

    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now(CLOCK)
            .plusDays(7);

    private ScreeningFixture() {
    }

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