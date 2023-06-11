package code.films;

import code.films.domain.Film;
import code.films.domain.FilmScreening;
import code.films.domain.FilmScreeningSeat;
import code.rooms.domain.Room;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public class FilmScreeningTestHelper {

    private static final int CURRENT_YEAR = Year.now().getValue();

    public static final LocalDateTime SCREENING_DATE = LocalDateTime.of(
            CURRENT_YEAR,
            5,
            10,
            18,
            30
    );

    public static FilmScreening createScreening(Film film, Room room) {
        var seats = List.of(
                FilmScreeningSeat.of(1, 2),
                FilmScreeningSeat.of(1, 2)
        );
        return FilmScreening.create(
                SCREENING_DATE,
                film,
                room,
                seats
        );
    }

    public static FilmScreening createScreening(Film film, Room room, LocalDateTime screeningDate) {
        var seats = List.of(
                FilmScreeningSeat.of(1, 2),
                FilmScreeningSeat.of(1, 2)
        );
        return FilmScreening.create(
                screeningDate,
                film,
                room,
                seats
        );
    }

    public static List<FilmScreening> createScreenings(Film film, Room room) {
        var screening1 = FilmScreening.create(
                SCREENING_DATE,
                film,
                room,
                List.of(
                        FilmScreeningSeat.of(1, 2),
                        FilmScreeningSeat.of(1, 2)
                )
        );
        var screening2 = FilmScreening.create(
                SCREENING_DATE.plusDays(1),
                film,
                room,
                List.of(
                        FilmScreeningSeat.of(1, 2),
                        FilmScreeningSeat.of(1, 2)
                )
        );
        return List.of(screening1, screening2);
    }

    public static List<String> getWrongScreeningDates() {
        var date1 = SCREENING_DATE.minusYears(1).toString();
        var date2 = SCREENING_DATE.plusYears(2).toString();
        return List.of(date1, date2);
    }
}