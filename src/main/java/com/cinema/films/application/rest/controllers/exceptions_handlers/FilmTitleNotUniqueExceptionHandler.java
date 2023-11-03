package com.cinema.films.application.rest.controllers.exceptions_handlers;

import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.shared.exceptions.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class FilmTitleNotUniqueExceptionHandler {

    @ExceptionHandler(FilmTitleNotUniqueException.class)
    ResponseEntity<ExceptionMessage> handle(FilmTitleNotUniqueException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
