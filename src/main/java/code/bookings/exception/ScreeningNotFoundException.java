package code.bookings.exception;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
