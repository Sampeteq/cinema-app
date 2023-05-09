package code.utils;

import code.films.client.commands.CreateFilmCommand;
import code.films.domain.Film;
import code.films.domain.FilmCategory;
import code.rooms.domain.Room;
import code.screenings.client.commands.CreateScreeningCommand;
import code.screenings.domain.Screening;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FilmTestHelper {

    private static final int CURRENT_YEAR = Year.now().getValue();

    public static CreateFilmCommand createCreateFilmCommand() {
        return CreateFilmCommand
                .builder()
                .title("Test film 1")
                .filmCategory(FilmCategory.COMEDY)
                .year(CURRENT_YEAR)
                .durationInMinutes(100)
                .build();
    }

    public static Film createFilm() {
        return Film
                .builder()
                .id(UUID.randomUUID())
                .title("Test film 1")
                .category(FilmCategory.COMEDY)
                .year(CURRENT_YEAR)
                .durationInMinutes(100)
                .screenings(new ArrayList<>())
                .build();
    }

    public static Film createFilmWithScreening(Room room) {
        var film = createFilm();
        var screening = createScreening(film, room);
        film.addScreening(screening);
        return film;
    }

    public static Film createFilmWithScreening(LocalDateTime screeningDate, Room room) {
        var film = createFilm();
        var screening = createScreening(film, room).withDate(screeningDate);
        film.addScreening(screening);
        return film;
    }

    public static List<Film> createFilms() {
        var film1 = Film
                .builder()
                .id(UUID.randomUUID())
                .title("Test film 1")
                .category(FilmCategory.COMEDY)
                .year(CURRENT_YEAR)
                .durationInMinutes(100)
                .screenings(Collections.emptyList())
                .build();
        var film2 = Film
                .builder()
                .id(UUID.randomUUID())
                .title("Test film 2")
                .category(FilmCategory.DRAMA)
                .year(CURRENT_YEAR + 1)
                .durationInMinutes(120)
                .screenings(Collections.emptyList())
                .build();
        return List.of(film1, film2);
    }

    public static List<Integer> getWrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }

    public static CreateScreeningCommand createCreateScreeningCommand(UUID filmId, UUID roomId) {
        return CreateScreeningCommand
                .builder()
                .filmId(filmId)
                .date(LocalDateTime.of(CURRENT_YEAR, 5, 10, 18, 30))
                .roomId(roomId)
                .build();
    }

    public static Screening createScreening(Film film, Room room) {
        return Screening.of(
                LocalDateTime.of(CURRENT_YEAR, 5, 10, 18, 30),
                film,
                room
        );
    }

    public static List<Screening> createScreenings(Film film, Room room) {
        var screening1 = Screening.of(
                LocalDateTime.of(CURRENT_YEAR, 5, 10, 18, 30),
                film,
                room
        );
        var screening2 = Screening.of(
                LocalDateTime.of(CURRENT_YEAR, 5, 11, 18, 30),
                film,
                room
        );
        return List.of(screening1, screening2);
    }


    public static List<String> getWrongScreeningDates() {
        var date1 = LocalDateTime.of(
                CURRENT_YEAR - 1,
                2,
                2,
                16,
                30
        ).toString();
        var date2 = LocalDateTime.of(
                CURRENT_YEAR + 2,
                2,
                2,
                16,
                30
        ).toString();
        return List.of(date1, date2);
    }
}
