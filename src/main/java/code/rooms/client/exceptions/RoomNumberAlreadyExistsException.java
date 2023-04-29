package code.rooms.client.exceptions;

import code.shared.ValidationException;

public class RoomNumberAlreadyExistsException extends ValidationException {

    public RoomNumberAlreadyExistsException() {
        super("A room number already exists");
    }
}
