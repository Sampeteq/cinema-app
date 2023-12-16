package com.cinema.films;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmCategory;

import java.time.Year;

public final class FilmFixture {

    private static final String TITLE = "TITLE 1";

    private static final FilmCategory CATEGORY = FilmCategory.COMEDY;

    private static final int YEAR = Year.now().getValue();

    private static final int DURATION_IN_MINUTES = 100;

    private FilmFixture() {
    }

    public static CreateFilm createCreateFilmCommand() {
        return new CreateFilm(
                TITLE,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static CreateFilm createCreateFilmCommand(String title) {
        return new CreateFilm(
                title,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static CreateFilm createCreateFilmCommand(Integer year) {
        return new CreateFilm(
                TITLE,
                CATEGORY,
                year,
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
}
