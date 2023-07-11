package code.catalog.helpers;

import code.catalog.domain.Film;
import code.catalog.domain.Room;
import code.catalog.domain.Screening;
import code.catalog.domain.Seat;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public class ScreeningTestHelper {

    private static final int CURRENT_YEAR = Year.now().getValue();

    public static final LocalDateTime SCREENING_DATE = LocalDateTime.of(
            CURRENT_YEAR,
            5,
            10,
            18,
            30
    );

    public static Screening createScreening(Film film, Room room) {
        return Screening.create(
                SCREENING_DATE,
                film,
                room
        );
    }

    public static Screening createScreening(Film film, Room room, LocalDateTime screeningDate) {
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

    public static List<String> getWrongScreeningDates() {
        var date1 = SCREENING_DATE.minusYears(1).toString();
        var date2 = SCREENING_DATE.plusYears(2).toString();
        return List.of(date1, date2);
    }
}