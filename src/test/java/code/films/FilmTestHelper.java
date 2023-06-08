package code.films;

import code.films.application.commands.FilmCreateCommand;
import code.films.domain.Film;
import code.films.domain.FilmCategory;
import code.rooms.domain.Room;
import code.screenings.application.commands.ScreeningCreateCommand;
import code.screenings.domain.Screening;
import code.screenings.domain.Seat;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public class FilmTestHelper {
    private static final int CURRENT_YEAR = Year.now().getValue();
    private static final LocalDateTime SCREENING_DATE = LocalDateTime.of(
            CURRENT_YEAR,
            5,
            10,
            18,
            30
    );

    public static FilmCreateCommand createCreateFilmCommand() {
        return FilmCreateCommand
                .builder()
                .title("Test film 1")
                .category(FilmCategory.COMEDY)
                .year(CURRENT_YEAR)
                .durationInMinutes(100)
                .build();
    }

    public static Film createFilm() {
        return Film.create("Test film 1", FilmCategory.COMEDY, CURRENT_YEAR, 100);
    }

    public static Film createFilm(FilmCategory category) {
        return Film.create("Test film 1", category, CURRENT_YEAR, 100);
    }

    public static List<Film> createFilms() {
        var film1 = Film.create("Test film 1", FilmCategory.COMEDY, CURRENT_YEAR, 100);
        var film2 = Film.create("Test film 2", FilmCategory.DRAMA, CURRENT_YEAR, 120);
        return List.of(film1, film2);
    }

    public static List<Integer> getWrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }

    public static ScreeningCreateCommand createCreateScreeningCommand(Long filmId) {
        return ScreeningCreateCommand
                .builder()
                .filmId(filmId)
                .date(SCREENING_DATE)
                .build();
    }

    public static Screening createScreening(Film film, Room room) {
        var screening = Screening.create(
                SCREENING_DATE,
                film,
                room
        ).withId(1L);
        var seat1 = Seat.of(1,2,screening);
        var seat2 = Seat.of(1,2,screening);
        screening.addSeats(List.of(seat1, seat2));
        return screening;
    }

    public static Screening createScreening(Film film, Room room, LocalDateTime screeningDate) {
        var screening = Screening.create(
                screeningDate,
                film,
                room
        ).withId(1L);
        var seat1 = Seat.of(1,2,screening);
        var seat2 = Seat.of(1,2,screening);
        screening.addSeats(List.of(seat1, seat2));
        return screening;
    }

    public static List<Screening> createScreenings(Film film, Room room) {
        var screening1 = Screening.create(
                SCREENING_DATE,
                film,
                room
        ).withId(1L);
        var seat11 = Seat.of(1,2,screening1);
        var seat12 = Seat.of(1,2,screening1);
        screening1.addSeats(List.of(seat11, seat12));
        var screening2 = Screening.create(
                SCREENING_DATE.plusDays(1),
                film,
                room
        ).withId(2L);
        var seat21 = Seat.of(1,2,screening2);
        var seat22 = Seat.of(1,2,screening2);
        screening2.addSeats(List.of(seat21, seat22));
        return List.of(screening1, screening2);
    }

    public static List<String> getWrongScreeningDates() {
        var date1 = SCREENING_DATE.minusYears(1).toString();
        var date2 = SCREENING_DATE.plusYears(2).toString();
        return List.of(date1, date2);
    }
}
