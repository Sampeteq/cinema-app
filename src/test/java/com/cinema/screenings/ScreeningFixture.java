package com.cinema.screenings;

import com.cinema.screenings.application.commands.CreateScreening;
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

    private ScreeningFixture() {
    }

    public static Screening createScreening(LocalDateTime screeningDate) {
        return new Screening(
                screeningDate,
                FILM_ID,
                HALL_ID
        );
    }

    public static CreateScreening createCreateScreeningCommand() {
        return new CreateScreening(
                SCREENING_DATE,
                FILM_ID
        );
    }

    public static CreateScreening createCreateScreeningCommand(LocalDateTime date) {
        return new CreateScreening(
                date,
                FILM_ID
        );
    }
}