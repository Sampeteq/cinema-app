package com.cinema.films.ui.rest.exceptions_handlers;

import com.cinema.films.domain.exceptions.FilmYearOutOfRangeException;
import com.cinema.shared.exceptions.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FilmYearOfRangeExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ExceptionMessage> handle(FilmYearOutOfRangeException exception) {
        var exceptionMessage = new ExceptionMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
