package com.cinema.screenings.ui;

import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import com.cinema.shared.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ScreeningExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(ScreeningDateOutOfRangeException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(ScreeningNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(ScreeningSeatNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
