package code.bookings.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class BookingTooLateException extends ValidationException {

    public BookingTooLateException() {
        super("Too late for booking");
    }
}
