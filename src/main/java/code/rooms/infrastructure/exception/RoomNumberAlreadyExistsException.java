package code.rooms.infrastructure.exception;

import code.rooms.domain.exception.RoomException;

public class RoomNumberAlreadyExistsException extends RoomException {

    public RoomNumberAlreadyExistsException() {
        super("A room number already exists");
    }
}
