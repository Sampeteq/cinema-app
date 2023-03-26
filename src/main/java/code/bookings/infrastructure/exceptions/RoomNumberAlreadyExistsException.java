package code.bookings.infrastructure.exceptions;

import code.bookings.domain.exceptions.RoomException;

public class RoomNumberAlreadyExistsException extends RoomException {

    public RoomNumberAlreadyExistsException() {
        super("A room number already exists");
    }
}
