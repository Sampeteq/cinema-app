package com.cinema.films.infrastrcture;

import com.cinema.films.domain.exceptions.FilmNotFoundException;
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
}
