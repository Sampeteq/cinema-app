package com.cinema.films;

import com.cinema.films.application.dto.CreateFilmDto;
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

    public static CreateFilmDto createCreateFilmDto() {
        return new CreateFilmDto(
                TITLE,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static CreateFilmDto createCreateFilmDto(String title) {
        return new CreateFilmDto(
                title,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static CreateFilmDto createCreateFilmDto(Integer year) {
        return new CreateFilmDto(
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
