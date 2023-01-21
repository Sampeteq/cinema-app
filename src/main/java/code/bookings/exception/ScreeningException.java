package code.bookings.exception;

public abstract class ScreeningException extends RuntimeException {

    public ScreeningException(String message) {
        super(message);
    }
}
