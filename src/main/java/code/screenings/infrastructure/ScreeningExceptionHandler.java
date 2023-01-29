package code.screenings.infrastructure;

import code.screenings.domain.exceptions.ScreeningException;
import code.screenings.infrastructure.exceptions.ScreeningNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScreeningExceptionHandler {

    @ExceptionHandler(ScreeningException.class)
    public ResponseEntity<String> handle(ScreeningException exception) {
        if (exception instanceof ScreeningNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
