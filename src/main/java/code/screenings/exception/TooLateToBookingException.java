package code.screenings.exception;

public class TooLateToBookingException extends BookingException {

    public TooLateToBookingException() {
        super("Too late to booking.");
    }
}
