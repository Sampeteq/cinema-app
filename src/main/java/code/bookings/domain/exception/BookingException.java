package code.bookings.domain.exception;

import code.screenings.domain.exception.ScreeningException;

public class BookingException extends ScreeningException {

    public BookingException(String message) {
        super(message);
    }
}
