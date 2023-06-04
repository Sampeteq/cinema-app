package code.rooms.domain.exceptions;

import code.shared.ValidationException;

public class RoomCustomIdAlreadyExistsException extends ValidationException {

    public RoomCustomIdAlreadyExistsException() {
        super("A room customId already exists");
    }
}
