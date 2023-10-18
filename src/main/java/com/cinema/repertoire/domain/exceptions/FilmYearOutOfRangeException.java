package com.cinema.repertoire.domain.exceptions;

public class FilmYearOutOfRangeException extends RuntimeException {

    public FilmYearOutOfRangeException() {
        super("A film year must be previous, current or next one");
    }
}
