package com.cinema.tickets.application.rest;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class TicketExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    ResponseEntity<ExceptionMessage> handle(TicketNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketAlreadyCancelledException.class)
    ResponseEntity<ExceptionMessage> handle(TicketAlreadyCancelledException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TicketAlreadyExists.class)
    ResponseEntity<ExceptionMessage> handle(TicketAlreadyExists exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TicketBookTooLateException.class)
    ResponseEntity<ExceptionMessage> handle(TicketBookTooLateException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TicketCancelTooLateException.class)
    ResponseEntity<ExceptionMessage> handle(TicketCancelTooLateException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TicketNotBelongsToUser.class)
    ResponseEntity<ExceptionMessage> handle(TicketNotBelongsToUser exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.FORBIDDEN);
    }
}
