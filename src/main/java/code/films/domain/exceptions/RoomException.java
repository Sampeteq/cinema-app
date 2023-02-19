package code.films.domain.exceptions;

public abstract class RoomException extends FilmException {

    protected RoomException(String message) {
        super(message);
    }
}
