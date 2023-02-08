package code.rooms.domain.exceptions;

public abstract class RoomException extends RuntimeException {

    public RoomException(String message) {
        super(message);
    }
}
