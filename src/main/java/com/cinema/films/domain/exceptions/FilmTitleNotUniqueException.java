package com.cinema.films.domain.exceptions;

public class FilmTitleNotUniqueException extends RuntimeException {

    public FilmTitleNotUniqueException() {
        super("Film title not unique");
    }
}
