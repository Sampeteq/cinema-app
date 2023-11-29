package com.cinema.users.ui.rest;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(UserMailNotUniqueException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(UserNotFoundException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }
}
