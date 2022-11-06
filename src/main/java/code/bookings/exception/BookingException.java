package code.bookings.exception;

public abstract class BookingException extends RuntimeException {

    public BookingException(String message) {
        super(message);
    }
}
