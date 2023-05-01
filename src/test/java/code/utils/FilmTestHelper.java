package code.utils;

import code.films.client.commands.CreateFilmCommand;
import code.screenings.client.commands.CreateScreeningCommand;
import code.films.domain.Film;
import code.films.domain.FilmCategory;
import code.screenings.domain.Screening;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static code.utils.RoomTestHelper.createSampleRoom;

public class FilmTestHelper {

    private static final int CURRENT_YEAR = Year.now().getValue();

    public static CreateFilmCommand createSampleCreateFilmCommand() {
        return CreateFilmCommand
                .builder()
                .title("Test film 1")
                .filmCategory(FilmCategory.COMEDY)
                .year(CURRENT_YEAR)
                .durationInMinutes(100)
                .build();
    }

    public static Film createSampleFilm() {
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

    public static Film createSampleFilmWithScreening() {
        var film = createSampleFilm();
        var screening = createSampleScreening(film);
        film.addScreening(screening);
        return film;
    }

    public static Film createSampleFilmWithScreening(LocalDateTime screeningDate) {
        var film = createSampleFilm();
        var screening = createSampleScreening(film).withDate(screeningDate);
        film.addScreening(screening);
        return film;
    }

    public static List<Film> createSampleFilms() {
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

    public static List<Integer> sampleWrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }

    public static CreateScreeningCommand createSampleCreateScreeningCommand(UUID filmId, UUID roomId) {
        return CreateScreeningCommand
                .builder()
                .filmId(filmId)
                .date(LocalDateTime.of(CURRENT_YEAR, 5, 10, 18, 30))
                .minAge(13)
                .roomId(roomId)
                .build();
    }

    public static Screening createSampleScreening(Film film) {
        return Screening.of(
                LocalDateTime.of(CURRENT_YEAR, 5, 10, 18, 30),
                13,
                film,
                createSampleRoom()
        );
    }

    public static List<Screening> createSampleScreenings(Film film) {
        var screening1 = Screening.of(
                LocalDateTime.of(CURRENT_YEAR, 5, 10, 18, 30),
                13,
                film,
                createSampleRoom()
        );
        var screening2 = Screening.of(
                LocalDateTime.of(CURRENT_YEAR, 5, 10, 18, 30),
                13,
                film,
                createSampleRoom()
        );
        return List.of(screening1, screening2);
    }


    public static List<String> sampleWrongScreeningDates() {
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
