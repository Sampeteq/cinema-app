package code.rooms.infrastructure.exceptions;

import code.rooms.domain.exceptions.RoomException;

public class RoomNotFoundException extends RoomException {

    public RoomNotFoundException() {
        super("Room not found");
    }
}
