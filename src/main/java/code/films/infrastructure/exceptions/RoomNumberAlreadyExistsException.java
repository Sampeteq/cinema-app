package code.films.infrastructure.exceptions;

import code.films.domain.exceptions.RoomException;

public class RoomNumberAlreadyExistsException extends RoomException {

    public RoomNumberAlreadyExistsException() {
        super("A room number already exists");
    }
}
