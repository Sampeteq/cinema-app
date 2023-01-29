package code.bookings.domain.exception;

public class SeatNotAvailableException extends BookingException {

    public SeatNotAvailableException() {
        super("Seat not available");
    }
}
