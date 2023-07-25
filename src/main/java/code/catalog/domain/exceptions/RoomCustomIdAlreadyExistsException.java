package code.catalog.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class RoomCustomIdAlreadyExistsException extends ValidationException {

    public RoomCustomIdAlreadyExistsException() {
        super("A room customId already exists");
    }
}
