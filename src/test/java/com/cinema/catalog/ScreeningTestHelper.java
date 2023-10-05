package com.cinema.catalog;

import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.Seat;

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
        var isFree = true;
        var seat = new Seat(1,2, isFree);
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