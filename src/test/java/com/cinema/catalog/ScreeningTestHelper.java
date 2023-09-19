package com.cinema.catalog;

import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.Room;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.Seat;

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
        var seat = Seat.create(1,2);
        return Screening.create(
                getScreeningDate(currentDate),
                film,
                room,
                List.of(seat)
        );
    }

    public static Screening createScreeningWithSpecificDate(
            Film film,
            Room room,
            LocalDateTime screeningDate
    ) {
        var seat = Seat.create(1,2);
        return Screening.create(
               screeningDate,
                film,
                room,
                List.of(seat)
        );
    }

    public static List<Screening> createScreenings(Film film, Room room) {
        var seat = Seat.create(1,2);
        var screening1 = Screening.create(
                SCREENING_DATE,
                film,
                room,
                List.of(seat)
        );
        var screening2 = Screening.create(
                SCREENING_DATE.plusDays(1),
                film,
                room,
                List.of(seat)
        );
        return List.of(screening1, screening2);
    }

    /** Difference between current and screening date must be at least 7 days */
    public static LocalDateTime getScreeningDate(LocalDateTime currentDate) {
        return currentDate.plusDays(7);
    }
}