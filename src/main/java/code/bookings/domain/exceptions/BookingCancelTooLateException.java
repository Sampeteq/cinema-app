package code.bookings.domain.exceptions;

import code.shared.ValidationException;

public class BookingCancelTooLateException extends ValidationException {

    public BookingCancelTooLateException() {
        super("Too late to cancel booking exception");
    }
}
