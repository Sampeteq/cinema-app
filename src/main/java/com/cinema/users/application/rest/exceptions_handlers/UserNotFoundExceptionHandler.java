package com.cinema.users.application.rest.exceptions_handlers;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserNotFoundExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ExceptionMessage> handle(UserNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
