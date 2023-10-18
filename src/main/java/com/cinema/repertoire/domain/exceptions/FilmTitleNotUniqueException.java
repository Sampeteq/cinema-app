package com.cinema.repertoire.domain.exceptions;

public class FilmTitleNotUniqueException extends RuntimeException {

    public FilmTitleNotUniqueException() {
        super("Film title not unique");
    }
}
