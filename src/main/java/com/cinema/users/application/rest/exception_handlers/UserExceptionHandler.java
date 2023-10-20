package com.cinema.users.application.rest.exception_handlers;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import com.cinema.users.domain.exceptions.UserNotSamePasswordsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ExceptionMessage> handle(UserNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserMailAlreadyExistsException.class)
    ResponseEntity<ExceptionMessage> handle(UserMailAlreadyExistsException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserNotSamePasswordsException.class)
    ResponseEntity<ExceptionMessage> handle(UserNotSamePasswordsException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
