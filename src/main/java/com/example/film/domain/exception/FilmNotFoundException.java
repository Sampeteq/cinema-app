package com.example.film.domain.exception;

public class FilmNotFoundException extends FilmException {
    public FilmNotFoundException(Long filmId) {
        super("Film not found: " + filmId);
    }
}
