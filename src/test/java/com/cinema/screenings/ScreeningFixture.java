package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.application.dto.CreateScreeningDto;
import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class ScreeningFixture {
    public static final Long HALL_ID = 1L;
    public static final Long FILM_ID = 1L;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);
    public static final LocalDateTime SCREENING_END_DATE = SCREENING_DATE.plusMinutes(100);

    private ScreeningFixture() {
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

    public static CreateScreeningDto createCreateScreeningDto() {
        return new CreateScreeningDto(
                SCREENING_DATE,
                FILM_ID,
                HALL_ID
        );
    }

    public static CreateScreeningDto createCreateScreeningDto(LocalDateTime date) {
        return new CreateScreeningDto(
                date,
                FILM_ID,
                HALL_ID
        );
    }
}