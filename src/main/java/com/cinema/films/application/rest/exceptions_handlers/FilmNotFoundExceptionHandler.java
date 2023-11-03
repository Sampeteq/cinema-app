package com.cinema.films.application.rest.exceptions_handlers;

import com.cinema.films.domain.exceptions.FilmNotFoundException;
import com.cinema.shared.exceptions.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class FilmNotFoundExceptionHandler {

    @ExceptionHandler(FilmNotFoundException.class)
    ResponseEntity<ExceptionMessage> handle(FilmNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
