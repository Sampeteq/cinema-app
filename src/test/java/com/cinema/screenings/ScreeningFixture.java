package com.cinema.screenings;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.FilmCategory;
import com.cinema.halls.infrastructure.config.CreateHall;
import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;

public final class ScreeningFixture {

    public static final Long FILM_ID = 1L;
    public static final String FILM_TITLE = "FILM 1";
    public static final FilmCategory FILM_CATEGORY = FilmCategory.COMEDY;
    public static final int FILM_YEAR = Year.now().getValue();
    public static final int FILM_DURATION_IN_MINUTES = 100;
    public static final String HALL_CUSTOM_ID = "1";
    public static final int HALL_ROWS_NUMBER = 10;
    public static final int HALL_ROW_SEATS_NUMBER = 15;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);

    private ScreeningFixture() {
    }

    public static Screening createScreening(LocalDateTime screeningDate) {
        var hallId = "1";
        return new Screening(
                screeningDate,
                FILM_ID,
                hallId
        );
    }

    public static CreateFilm createCreateFilmCommand() {
        return new CreateFilm(
                FILM_TITLE,
                FILM_CATEGORY,
                FILM_YEAR,
                FILM_DURATION_IN_MINUTES
        );
    }

    public static CreateFilm createCreateFilmCommand(String filmTitle) {
        return new CreateFilm(
                filmTitle,
                FILM_CATEGORY,
                FILM_YEAR,
                FILM_DURATION_IN_MINUTES
        );
    }

    public static CreateHall createCreateHallCommand() {
        return new CreateHall(
                HALL_CUSTOM_ID,
                HALL_ROWS_NUMBER,
                HALL_ROW_SEATS_NUMBER
        );
    }
}