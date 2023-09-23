package com.cinema.catalog;

import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.Seat;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.cinema.TimeHelper.getLocalDateTime;

public final class ScreeningTestHelper {

    private ScreeningTestHelper() {
    }

    public static final LocalDateTime SCREENING_DATE = getLocalDateTime();

    public static Screening createScreening(
            Film film,
            LocalDateTime screeningDate
    ) {
        var isFree = true;
        var seat = new Seat(1,2, isFree);
        var roomCustomId = "1";
        var screeningEndDate = film.calculateScreeningEndDate(screeningDate);
        return new Screening(
               screeningDate,
                screeningEndDate,
                film,
                roomCustomId,
                List.of(seat)
        );
    }

    public static List<Screening> createScreenings(Film film) {
        var isFree = true;
        var seat = new Seat(1,2, isFree);
        var roomCustomId = "1";
        var screeningEndDate1 = film.calculateScreeningEndDate(SCREENING_DATE);
        var screening1 = new Screening(
                SCREENING_DATE,
                screeningEndDate1,
                film,
                roomCustomId,
                List.of(seat)
        );
        var screeningEndDate2 = film.calculateScreeningEndDate(SCREENING_DATE.plusDays(1));
        var screening2 = new Screening(
                SCREENING_DATE.plusDays(1),
                screeningEndDate2,
                film,
                roomCustomId,
                List.of(seat)
        );
        return List.of(screening1, screening2);
    }

    /** Difference between current and screening date must be at least 7 days */
    public static LocalDateTime getScreeningDate(Clock clock) {
        return LocalDateTime.now(clock).plusDays(7);
    }
}