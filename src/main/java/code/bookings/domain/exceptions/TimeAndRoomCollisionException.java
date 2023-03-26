package code.bookings.domain.exceptions;

public class TimeAndRoomCollisionException extends ScreeningException {

    public TimeAndRoomCollisionException() {
        super("Time and room collision between screenings");
    }
}
