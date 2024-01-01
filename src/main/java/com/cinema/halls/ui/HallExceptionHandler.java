package com.cinema.halls.ui;

import com.cinema.halls.domain.exceptions.HallNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import com.cinema.shared.exceptions.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
class HallExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(ScreeningsCollisionsException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(HallNotFoundException exception) {
        log.error("Exception:{}", exception.getMessage());
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
