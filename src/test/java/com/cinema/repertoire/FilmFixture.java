package com.cinema.repertoire;

import com.cinema.repertoire.application.dto.FilmCreateDto;
import com.cinema.repertoire.domain.Film;
import com.cinema.repertoire.domain.FilmCategory;

import java.time.Year;
import java.util.List;

public final class FilmFixture {

    private static final String TITLE = "TITLE 1";

    private static final FilmCategory CATEGORY = FilmCategory.COMEDY;

    private static final int YEAR = Year.now().getValue();

    private static final int DURATION_IN_MINUTES = 100;

    private FilmFixture() {
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
        return new Film(
                TITLE,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static Film createFilm(FilmCategory category) {
        return new Film(
                TITLE,
                category,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static Film createFilm(String title) {
        return new Film(
                title,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static List<Integer> getWrongFilmYears() {
        return List.of(
                YEAR - 2,
                YEAR + 2
        );
    }
}
