package code.rooms.exception;

public class RoomNotFoundException extends RoomException {

    public RoomNotFoundException() {
        super("Screening room not found");
    }
}
