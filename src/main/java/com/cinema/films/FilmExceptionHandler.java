package com.cinema.films;

import com.cinema.films.exceptions.FilmNotFoundException;
import com.cinema.films.exceptions.FilmTitleNotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class FilmExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handle(FilmNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    ResponseEntity<String> handle(FilmTitleNotUniqueException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
