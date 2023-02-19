package code.films.infrastructure.rest;

import code.films.domain.exceptions.RoomException;
import code.films.infrastructure.exceptions.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<?> handle(RoomException exception) {
        if (exception instanceof RoomNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
