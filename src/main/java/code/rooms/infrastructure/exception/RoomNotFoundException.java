package code.rooms.infrastructure.exception;

import code.rooms.domain.exception.RoomException;

public class RoomNotFoundException extends RoomException {

    public RoomNotFoundException() {
        super("Room not found");
    }
}
