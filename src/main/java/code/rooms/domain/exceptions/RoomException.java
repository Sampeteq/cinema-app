package code.rooms.domain.exceptions;

import code.films.domain.exceptions.FilmException;

public abstract class RoomException extends FilmException {

    protected RoomException(String message) {
        super(message);
    }
}
