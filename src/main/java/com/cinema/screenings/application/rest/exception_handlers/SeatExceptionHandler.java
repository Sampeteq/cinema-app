package com.cinema.screenings.application.rest.exception_handlers;

import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class SeatExceptionHandler {

    @ExceptionHandler(SeatNotFoundException.class)
    ResponseEntity<ExceptionMessage> handle(SeatNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
