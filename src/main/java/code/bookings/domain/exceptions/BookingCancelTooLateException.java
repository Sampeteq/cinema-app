package code.bookings.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class BookingCancelTooLateException extends ValidationException {

    public BookingCancelTooLateException() {
        super("Too late to cancel booking exception");
    }
}
