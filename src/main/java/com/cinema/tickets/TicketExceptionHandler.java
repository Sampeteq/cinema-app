package com.cinema.tickets;

import com.cinema.tickets.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.exceptions.TicketBookTooLateException;
import com.cinema.tickets.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.exceptions.TicketNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class TicketExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handle(TicketAlreadyBookedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    ResponseEntity<String> handle(TicketBookTooLateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    ResponseEntity<String> handle(TicketCancelTooLateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    ResponseEntity<String> handle(TicketNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}