package com.cinema.films.exceptions;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException() {
        super("Film not found");
    }
}
