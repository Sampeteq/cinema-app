package code.bookings.domain.exceptions;

import code.shared.ValidationException;

public class BookingTooLateException extends ValidationException {

    public BookingTooLateException() {
        super("Too late for booking");
    }
}
