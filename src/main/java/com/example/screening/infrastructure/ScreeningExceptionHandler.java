package com.example.screening.infrastructure;

import com.example.screening.domain.exception.NotCurrentScreeningYearException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ScreeningExceptionHandler {

    @ExceptionHandler(NotCurrentScreeningYearException.class)
    ResponseEntity<?> handle(NotCurrentScreeningYearException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
