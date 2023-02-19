package code.films.infrastructure.exceptions;

import code.films.domain.exceptions.RoomException;

public class RoomNotFoundException extends RoomException {

    public RoomNotFoundException() {
        super("Room not found");
    }
}
