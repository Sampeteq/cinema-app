package com.cinema.screenings;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.FilmCategory;
import com.cinema.rooms.application.commands.CreateRoom;
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
    public static final String ROOM_CUSTOM_ID = "1";
    public static final int ROOM_ROWS_NUMBER = 10;
    public static final int ROOM_ROW_SEATS_NUMBER = 15;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);

    private ScreeningFixture() {
    }

    public static Screening createScreening(LocalDateTime screeningDate) {
        var roomId = "1";
        return new Screening(
                screeningDate,
                FILM_ID,
                roomId
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

    public static CreateRoom createCreateRoomCommand() {
        return new CreateRoom(
                ROOM_CUSTOM_ID,
                ROOM_ROWS_NUMBER,
                ROOM_ROW_SEATS_NUMBER
        );
    }
}