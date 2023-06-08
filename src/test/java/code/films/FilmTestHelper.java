package code.films;

import code.films.application.commands.FilmCreateCommand;
import code.films.domain.Film;
import code.films.domain.FilmCategory;

import java.time.Year;
import java.util.List;

public class FilmTestHelper {
    private static final int CURRENT_YEAR = Year.now().getValue();

    public static FilmCreateCommand createFilmCreateCommand() {
        return new FilmCreateCommand(
                "Test film 1",
                FilmCategory.COMEDY,
                CURRENT_YEAR,
                100
        );
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

}
