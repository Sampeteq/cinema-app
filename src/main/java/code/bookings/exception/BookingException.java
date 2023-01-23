package code.bookings.exception;

import code.screenings.exception.ScreeningException;

public class BookingException extends ScreeningException {

    public BookingException(String message) {
        super(message);
    }
}
