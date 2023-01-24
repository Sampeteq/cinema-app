package code.rooms.infrastructure;

import code.rooms.exception.RoomException;
import code.rooms.exception.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class RoomExceptionHandler {

    @ExceptionHandler(RoomException.class)
    ResponseEntity<?> handle(RoomException exception) {
        if (exception instanceof RoomNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
