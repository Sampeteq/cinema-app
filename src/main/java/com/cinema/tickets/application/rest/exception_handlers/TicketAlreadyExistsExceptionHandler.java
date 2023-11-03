package com.cinema.tickets.application.rest.exception_handlers;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class TicketAlreadyExistsExceptionHandler {

    @ExceptionHandler(TicketAlreadyExistsException.class)
    ResponseEntity<ExceptionMessage> handle(TicketAlreadyExistsException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
