package com.cinema.catalog.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class FilmYearOutOfRangeException extends ValidationException {

    public FilmYearOutOfRangeException() {
        super("A film year must be previous, current or next one");
    }
}
