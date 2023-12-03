package com.cinema.halls.ui.rest;

import com.cinema.halls.domain.exceptions.HallsNoAvailableException;
import com.cinema.shared.exceptions.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class HallExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(HallsNoAvailableException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}