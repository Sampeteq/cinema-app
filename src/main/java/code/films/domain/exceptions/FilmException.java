package code.films.domain.exceptions;

public abstract class FilmException extends RuntimeException {

    protected FilmException(String message) {
        super(message);
    }
}
