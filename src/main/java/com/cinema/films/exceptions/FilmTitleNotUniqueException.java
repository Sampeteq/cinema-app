package com.cinema.films.exceptions;

public class FilmTitleNotUniqueException extends RuntimeException {

    public FilmTitleNotUniqueException() {
        super("Film title not unique");
    }
}
