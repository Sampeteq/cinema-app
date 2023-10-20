package com.cinema.films.application.rest;

import com.cinema.films.domain.exceptions.FilmNotFoundException;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.films.domain.exceptions.FilmYearOutOfRangeException;
import com.cinema.shared.exceptions.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class FilmExceptionHandler {

    @ExceptionHandler(FilmNotFoundException.class)
    ResponseEntity<ExceptionMessage> handle(FilmNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FilmTitleNotUniqueException.class)
    ResponseEntity<ExceptionMessage> handle(FilmTitleNotUniqueException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(FilmYearOutOfRangeException.class)
    ResponseEntity<ExceptionMessage> handle(FilmYearOutOfRangeException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
