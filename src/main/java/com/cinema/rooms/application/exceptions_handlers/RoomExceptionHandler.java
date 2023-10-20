package com.cinema.rooms.application.exceptions_handlers;

import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import com.cinema.shared.exceptions.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class RoomExceptionHandler {

    @ExceptionHandler(RoomsNoAvailableException.class)
    ResponseEntity<ExceptionMessage> handle(RoomsNoAvailableException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
