package code.catalog.helpers;

import code.catalog.domain.Film;
import code.catalog.domain.Room;
import code.catalog.domain.Screening;

import java.time.LocalDateTime;
import java.util.List;

import static code.TimeHelper.getLocalDateTime;

public final class ScreeningTestHelper {

    private ScreeningTestHelper() {
    }

    public static final LocalDateTime SCREENING_DATE = getLocalDateTime();

    public static Screening createScreening(
            Film film,
            Room room,
            LocalDateTime currentDate
    ) {
        return Screening.create(
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
        return Screening.create(
               screeningDate,
                film,
                room
        );
    }

    public static List<Screening> createScreenings(Film film, Room room) {
        var screening1 = Screening.create(
                SCREENING_DATE,
                film,
                room
        );
        var screening2 = Screening.create(
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