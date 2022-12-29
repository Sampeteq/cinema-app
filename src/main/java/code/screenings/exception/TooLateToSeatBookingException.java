package code.screenings.exception;

public class TooLateToSeatBookingException extends BookingException {

    public TooLateToSeatBookingException() {
        super("Too late to booking.");
    }
}
