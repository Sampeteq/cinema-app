package code.bookings.domain.exceptions;

public class BookingException extends RuntimeException {

    public BookingException(String message) {
        super(message);
    }
}
