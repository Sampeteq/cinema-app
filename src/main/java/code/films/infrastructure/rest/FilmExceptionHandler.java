package code.films.infrastructure.rest;

import code.films.domain.exceptions.FilmException;
import code.films.infrastructure.exceptions.FilmNotFoundException;
import code.rooms.infrastructure.exceptions.RoomNotFoundException;
import code.films.infrastructure.exceptions.ScreeningNotFoundException;
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
