package code.rooms.domain.exceptions;

public abstract class RoomException extends RuntimeException {

    protected RoomException(String message) {
        super(message);
    }
}
