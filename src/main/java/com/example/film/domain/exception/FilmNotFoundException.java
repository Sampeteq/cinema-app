package com.example.film.domain.exception;

import java.util.UUID;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(UUID filmId) {
        super("Film not found: " + filmId);
    }
}
