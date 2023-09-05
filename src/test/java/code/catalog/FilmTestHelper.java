package code.catalog;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;

import java.time.Year;
import java.util.List;

public final class FilmTestHelper {

    public static final int CURRENT_YEAR = Year.now().getValue();

    private FilmTestHelper() {
    }

    public static FilmCreateDto createFilmCreateDto() {
        var title = "Test film 1";
        var category = FilmCategory.COMEDY;
        var duration = 100;
        return new FilmCreateDto(
                title,
                category,
                CURRENT_YEAR,
                duration
        );
    }

    public static Film createFilm() {
        var title = "Test film 1";
        var category = FilmCategory.COMEDY;
        var duration = 100;
        return Film.create(
                title,
                category,
                CURRENT_YEAR,
                duration
        );
    }

    public static Film createFilm(FilmCategory filmCategory) {
        var title = "Test film 1";
        var duration = 100;
        return Film.create(
                title,
                filmCategory,
                CURRENT_YEAR,
                duration
        );
    }

    public static Film createFilm(String filmTitle) {
        var category = FilmCategory.COMEDY;
        var duration = 100;
        return Film.create(
                filmTitle,
                category,
                CURRENT_YEAR,
                duration
        );
    }

    public static List<Film> createFilms() {
        var title1 = "Test film 1";
        var category1 = FilmCategory.COMEDY;
        var duration1 = 100;

        var title2 = "Test film 2";
        var category2 = FilmCategory.DRAMA;
        var year2 = CURRENT_YEAR + 1;
        var duration2 = 120;
        var film1 = Film.create(
                title1,
                category1,
                CURRENT_YEAR,
                duration1
        );
        var film2 = Film.create(
                title2,
                category2,
                year2,
                duration2
        );
        return List.of(film1, film2);
    }

    public static List<Film> createFilms(FilmCategory filmCategory) {
        var title1 = "Test film 1";
        var duration1 = 100;

        var title2 = "Test film 2";
        var year2 = CURRENT_YEAR + 1;
        var duration2 = 120;

        var film1 = Film.create(
                title1,
                filmCategory,
                CURRENT_YEAR,
                duration1
        );
        var film2 = Film.create(
                title2,
                filmCategory,
                year2,
                duration2
        );
        return List.of(film1, film2);
    }

    public static List<Integer> getWrongFilmYears() {
        var currentYear = CURRENT_YEAR;
        return List.of(
                currentYear - 2,
                currentYear + 2
        );
    }
}
