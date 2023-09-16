package com.cinema.catalog;

import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.Room;
import com.cinema.catalog.domain.Screening;

import java.time.LocalDateTime;
import java.util.List;

import static com.cinema.TimeHelper.getLocalDateTime;

public final class ScreeningTestHelper {

    private ScreeningTestHelper() {
    }

    public static final LocalDateTime SCREENING_DATE = getLocalDateTime();

    public static Screening createScreening(
            Film film,
            Room room,
            LocalDateTime currentDate
    ) {
        return new Screening(
                getScreeningDate(currentDate),
                film,
                room
        );
    }

    public static Screening createScreeningWithSpecificDate(
            Film film,
            Room room,
            LocalDateTime screeningDate
    ) {
        return new Screening(
               screeningDate,
                film,
                room
        );
    }

    public static List<Screening> createScreenings(Film film, Room room) {
        var screening1 = new Screening(
                SCREENING_DATE,
                film,
                room
        );
        var screening2 = new Screening(
                SCREENING_DATE.plusDays(1),
                film,
                room
        );
        return List.of(screening1, screening2);
    }

    /** Difference between current and screening date must be at least 7 days */
    public static LocalDateTime getScreeningDate(LocalDateTime currentDate) {
        return currentDate.plusDays(7);
    }
}