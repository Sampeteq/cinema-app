package code.films.domain.exceptions;

public abstract class ScreeningException extends FilmException {

    protected ScreeningException(String message) {
        super(message);
    }
}
