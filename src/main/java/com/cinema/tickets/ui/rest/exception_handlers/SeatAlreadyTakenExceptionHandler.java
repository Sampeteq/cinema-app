package com.cinema.tickets.ui.rest.exception_handlers;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.tickets.domain.exceptions.SeatAlreadyTakenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class SeatAlreadyTakenExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(SeatAlreadyTakenException exception) {
        return ResponseEntity
                .unprocessableEntity()
                .body(new ExceptionMessage(exception.getMessage()));
    }
}
