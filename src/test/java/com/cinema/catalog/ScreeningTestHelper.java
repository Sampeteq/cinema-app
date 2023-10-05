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

    public static final LocalDateTime SCREENING_DATE = LocalDateTime.of(2023, 12, 13, 16, 30);

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

    public static List<Screening> createScreenings(Film film) {
        var isFree = true;
        var seat = new Seat(1,2, isFree);
        var roomId = "1";
        var screening1 = new Screening(
                SCREENING_DATE,
                film,
                roomId,
                List.of(seat)
        );
        var screening2 = new Screening(
                SCREENING_DATE.plusDays(1),
                film,
                roomId,
                List.of(seat)
        );
        return List.of(screening1, screening2);
    }

    /** Difference between current and screening date must be at least 7 days */
    public static LocalDateTime getScreeningDate(Clock clock) {
        return LocalDateTime.now(clock).plusDays(7);
    }
}