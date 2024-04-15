package com.cinema.users.infrastructure.ui;

import com.cinema.users.application.exceptions.UserMailNotUniqueException;
import com.cinema.users.application.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handle(UserMailNotUniqueException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    ResponseEntity<String> handle(UserNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
