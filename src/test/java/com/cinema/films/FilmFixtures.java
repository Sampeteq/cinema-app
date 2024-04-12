package com.cinema.films;

import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.application.dto.FilmCreateDto;

import java.time.Year;
import java.util.UUID;

public class FilmFixtures {

    private static final UUID ID = UUID.randomUUID();

    private static final String TITLE = "TITLE 1";

    private static final FilmCategory CATEGORY = FilmCategory.COMEDY;

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
                ID,
                TITLE,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static Film createFilm(FilmCategory category) {
        return new Film(
                ID,
                TITLE,
                category,
                YEAR,
                DURATION_IN_MINUTES
        );
    }

    public static Film createFilm(String title) {
        return new Film(
                ID,
                title,
                CATEGORY,
                YEAR,
                DURATION_IN_MINUTES
        );
    }
}
