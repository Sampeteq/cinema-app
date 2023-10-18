package com.cinema.repertoire;

import com.cinema.repertoire.domain.Film;
import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.Seat;
import com.cinema.repertoire.domain.SeatStatus;
import com.cinema.rooms.application.dto.RoomCreateDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class ScreeningFixture {

    public static final String ROOM_CUSTOM_ID = "1";
    public static final int ROOM_ROWS_NUMBER = 10;
    public static final int ROOM_ROW_SEATS_NUMBER = 15;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);

    private ScreeningFixture() {
    }

    public static Screening createScreening(
            Film film,
            LocalDateTime screeningDate
    ) {
        var seat = new Seat(1,2, SeatStatus.FREE);
        var roomId = "1";
        return new Screening(
               screeningDate,
                film,
                roomId,
                List.of(seat)
        );
    }

    public static RoomCreateDto createRoomCreateDto() {
        return new RoomCreateDto(
                ROOM_CUSTOM_ID,
                ROOM_ROWS_NUMBER,
                ROOM_ROW_SEATS_NUMBER
        );
    }

    public static LocalDateTime getScreeningDate(Clock clock) {
        return LocalDateTime.now(clock).plusDays(7);
    }
}