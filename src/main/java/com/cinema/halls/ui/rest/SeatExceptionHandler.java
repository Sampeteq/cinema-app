package com.cinema.halls.ui.rest;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.halls.domain.exceptions.SeatNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class SeatExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(SeatNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
