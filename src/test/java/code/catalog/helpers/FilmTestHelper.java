package code.catalog.helpers;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;

import java.time.Year;
import java.util.List;

public final class FilmTestHelper {

    private FilmTestHelper() {
    }

    public static FilmCreateDto createFilmCreateDto() {
        var title = "Test film 1";
        var category = FilmCategory.COMEDY;
        var year = Year.now().getValue();
        var duration = 100;
        return new FilmCreateDto(
                title,
                category,
                year,
                duration
        );
    }

    public static Film createFilm() {
        var title = "Test film 1";
        var category = FilmCategory.COMEDY;
        var year = Year.now().getValue();
        var duration = 100;
        return Film.create(
                title,
                category,
                year,
                duration
        );
    }

    public static Film createFilm(FilmCategory filmCategory) {
        var title = "Test film 1";
        var year = Year.now().getValue();
        var duration = 100;
        return Film.create(
                title,
                filmCategory,
                year,
                duration
        );
    }

    public static Film createFilm(String filmTitle) {
        var category = FilmCategory.COMEDY;
        var year = Year.now().getValue();
        var duration = 100;
        return Film.create(
                filmTitle,
                category,
                year,
                duration
        );
    }

    public static List<Film> createFilms() {
        var title1 = "Test film 1";
        var category1 = FilmCategory.COMEDY;
        var year1 = Year.now().getValue();
        var duration1 = 100;

        var title2 = "Test film 2";
        var category2 = FilmCategory.DRAMA;
        var year2 = Year.now().getValue() + 1;
        var duration2 = 120;
        var film1 = Film.create(
                title1,
                category1,
                year1,
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
        var year1 = Year.now().getValue();
        var duration1 = 100;

        var title2 = "Test film 2";
        var year2 = Year.now().getValue() + 1;
        var duration2 = 120;

        var film1 = Film.create(
                title1,
                filmCategory,
                year1,
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
        var currentYear = Year.now().getValue();
        return List.of(
                currentYear - 2,
                currentYear + 2
        );
    }
}
