package code.bookings.exception;

public class RoomNotFoundException extends ScreeningException {

    public RoomNotFoundException() {
        super("Screening room not found");
    }
}
