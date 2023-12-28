package com.cinema.halls.ui;

import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import com.cinema.shared.exceptions.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class HallExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(ScreeningsCollisionsException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
