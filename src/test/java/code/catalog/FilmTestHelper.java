package code.catalog;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;

import java.time.Year;
import java.util.List;

public final class FilmTestHelper {

    private static final String TITLE = "TITLE 1";

    private static final FilmCategory CATEGORY = FilmCategory.COMEDY;

    private static final int YEAR = Year.now().getValue();

    private static final int DURATION_IN_MINUTES = 100;

    private FilmTestHelper() {
    }

    public static FilmCreateDto createFilmCreateDto() {
        return new FilmCreateDto(
                TITLE,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static Film createFilm() {
        return Film.create(
                TITLE,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static Film createFilm(FilmCategory category) {
        return Film.create(
                TITLE,
                category,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static Film createFilm(String title) {
        return Film.create(
                title,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static List<Film> createFilms() {
        var film1 = Film.create(
                TITLE,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
        var film2 = Film.create(
                TITLE + "another",
                FilmCategory.DRAMA,
                YEAR + 1,
                DURATION_IN_MINUTES + 30
        );
        return List.of(film1, film2);
    }

    public static List<Film> createFilms(FilmCategory filmCategory) {
        var film1 = Film.create(
                TITLE,
                filmCategory,
                YEAR,
                DURATION_IN_MINUTES
        );
        var film2 = Film.create(
                TITLE + "another",
                filmCategory,
                YEAR + 1,
                DURATION_IN_MINUTES + 30
        );
        return List.of(film1, film2);
    }

    public static List<Integer> getWrongFilmYears() {
        return List.of(
                YEAR - 2,
                YEAR + 2
        );
    }
}
