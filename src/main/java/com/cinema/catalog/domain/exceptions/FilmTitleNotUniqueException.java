package com.cinema.catalog.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FilmTitleNotUniqueException extends ValidationException {

    public FilmTitleNotUniqueException() {
        super("Film filmTitle not unique");
    }
}
