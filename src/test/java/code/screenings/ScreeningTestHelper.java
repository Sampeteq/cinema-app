package code.screenings;

import code.films.domain.Film;
import code.rooms.domain.Room;
import code.screenings.application.commands.ScreeningCreateCommand;
import code.screenings.domain.Screening;
import code.screenings.domain.Seat;

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

    public static ScreeningCreateCommand createCreateScreeningCommand(Long filmId) {
        return ScreeningCreateCommand
                .builder()
                .filmId(filmId)
                .date(SCREENING_DATE)
                .build();
    }

    public static Screening createScreening(Film film, Room room) {
        var seats = List.of(
                Seat.of(1, 2),
                Seat.of(1, 2)
        );
        return Screening.create(
                SCREENING_DATE,
                film,
                room,
                seats
        );
    }

    public static Screening createScreening(Film film, Room room, LocalDateTime screeningDate) {
        var seats = List.of(
                Seat.of(1, 2),
                Seat.of(1, 2)
        );
        return Screening.create(
                screeningDate,
                film,
                room,
                seats
        );
    }

    public static List<Screening> createScreenings(Film film, Room room) {
        var screening1 = Screening.create(
                SCREENING_DATE,
                film,
                room,
                List.of(
                        Seat.of(1, 2),
                        Seat.of(1, 2)
                )
        );
        var screening2 = Screening.create(
                SCREENING_DATE.plusDays(1),
                film,
                room,
                List.of(
                        Seat.of(1, 2),
                        Seat.of(1, 2)
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