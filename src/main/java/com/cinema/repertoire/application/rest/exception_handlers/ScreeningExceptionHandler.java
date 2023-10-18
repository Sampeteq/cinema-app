package com.cinema.repertoire.application.rest.exception_handlers;

import com.cinema.repertoire.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.repertoire.domain.exceptions.ScreeningNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ScreeningExceptionHandler {

    @ExceptionHandler(ScreeningNotFoundException.class)
    ResponseEntity<ExceptionMessage> handle(ScreeningNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScreeningDateOutOfRangeException.class)
    ResponseEntity<ExceptionMessage> handle(ScreeningDateOutOfRangeException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
