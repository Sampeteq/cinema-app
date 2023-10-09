package com.cinema.repertoire;

import com.cinema.repertoire.domain.Film;
import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.Seat;
import com.cinema.repertoire.domain.SeatStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public final class ScreeningTestHelper {

    private ScreeningTestHelper() {
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

    public static LocalDateTime getScreeningDate(Clock clock) {
        return LocalDateTime.now(clock).plusDays(7);
    }
}