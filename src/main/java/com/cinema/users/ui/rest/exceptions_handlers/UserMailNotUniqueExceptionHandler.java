package com.cinema.users.ui.rest.exceptions_handlers;

import com.cinema.shared.exceptions.ExceptionMessage;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserMailNotUniqueExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(UserMailNotUniqueException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
