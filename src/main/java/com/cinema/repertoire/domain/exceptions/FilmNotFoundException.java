package com.cinema.repertoire.domain.exceptions;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException() {
        super("Film not found");
    }
}
