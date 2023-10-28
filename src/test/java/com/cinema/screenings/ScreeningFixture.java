package com.cinema.screenings;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.FilmCategory;
import com.cinema.rooms.application.dto.RoomCreateDto;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.Seat;
import com.cinema.screenings.domain.SeatStatus;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        var seat = new Seat(1,2, SeatStatus.FREE);
        var roomId = "1";
        return new Screening(
                screeningDate,
                FILM_ID,
                roomId,
                List.of(seat)
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

    public static RoomCreateDto createRoomCreateDto() {
        return new RoomCreateDto(
                ROOM_CUSTOM_ID,
                ROOM_ROWS_NUMBER,
                ROOM_ROW_SEATS_NUMBER
        );
    }
}