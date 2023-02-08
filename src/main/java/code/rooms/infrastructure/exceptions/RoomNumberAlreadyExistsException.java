package code.rooms.infrastructure.exceptions;

import code.rooms.domain.exceptions.RoomException;

public class RoomNumberAlreadyExistsException extends RoomException {

    public RoomNumberAlreadyExistsException() {
        super("A room number already exists");
    }
}
