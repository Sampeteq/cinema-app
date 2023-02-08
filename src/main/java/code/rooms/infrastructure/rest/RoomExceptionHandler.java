package code.rooms.infrastructure.rest;

import code.rooms.domain.exceptions.RoomException;
import code.rooms.infrastructure.exceptions.RoomNotFoundException;
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
