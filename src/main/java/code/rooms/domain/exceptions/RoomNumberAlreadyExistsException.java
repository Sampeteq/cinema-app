package code.rooms.domain.exceptions;

import code.shared.ValidationException;

public class RoomNumberAlreadyExistsException extends ValidationException {

    public RoomNumberAlreadyExistsException() {
        super("A room customId already exists");
    }
}
