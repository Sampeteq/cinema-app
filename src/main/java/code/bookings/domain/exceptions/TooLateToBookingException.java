package code.bookings.domain.exceptions;

import code.shared.ValidationException;

public class TooLateToBookingException extends ValidationException {

    public TooLateToBookingException() {
        super("Too late for booking");
    }
}
