package code.bookings.domain.exceptions;

public abstract class FilmException extends RuntimeException {

    protected FilmException(String message) {
        super(message);
    }
}
