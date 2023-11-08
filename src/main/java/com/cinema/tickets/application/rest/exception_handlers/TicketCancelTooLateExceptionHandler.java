package com.cinema.tickets.application.rest.exception_handlers;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class TicketCancelTooLateExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(TicketCancelTooLateException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
