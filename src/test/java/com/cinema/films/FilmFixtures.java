package com.cinema.films;

import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmCreateDto;

import java.time.Year;

public class FilmFixtures {

    private static final String TITLE = "TITLE 1";

    private static final Film.Category CATEGORY = Film.Category.COMEDY;

    private static final int YEAR = Year.now().getValue();

    private static final int DURATION_IN_MINUTES = 100;

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

    public static Film createFilm(Film.Category category) {
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
