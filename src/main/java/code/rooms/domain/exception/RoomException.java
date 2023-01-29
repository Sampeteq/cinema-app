package code.rooms.domain.exception;

public abstract class RoomException extends RuntimeException {

    public RoomException(String message) {
        super(message);
    }
}
