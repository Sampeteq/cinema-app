package code.rooms.infrastructure.exceptions;

import code.bookings.domain.exceptions.RoomException;

public class RoomNotFoundException extends RoomException {

    public RoomNotFoundException() {
        super("Room not found");
    }
}
