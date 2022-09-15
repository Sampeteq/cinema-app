package com.example.film.domain.exception;

import com.example.film.domain.FilmId;

public class FilmNotFoundException extends FilmException {
    public FilmNotFoundException(FilmId filmId) {
        super("Film not found: " + filmId);
    }
}
