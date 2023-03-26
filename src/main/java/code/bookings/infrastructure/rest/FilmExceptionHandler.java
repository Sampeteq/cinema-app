package code.bookings.infrastructure.rest;

import code.bookings.domain.exceptions.FilmException;
import code.bookings.infrastructure.exceptions.FilmNotFoundException;
import code.bookings.infrastructure.exceptions.RoomNotFoundException;
import code.bookings.infrastructure.exceptions.ScreeningNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FilmExceptionHandler {

    @ExceptionHandler(FilmException.class)
    public ResponseEntity<String> handle(FilmException exception) {
        if (
                exception instanceof FilmNotFoundException ||
                exception instanceof ScreeningNotFoundException ||
                exception instanceof RoomNotFoundException
        ) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
