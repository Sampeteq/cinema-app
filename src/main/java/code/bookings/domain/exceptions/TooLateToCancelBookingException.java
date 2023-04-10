package code.bookings.domain.exceptions;

import code.shared.ValidationException;

public class TooLateToCancelBookingException extends ValidationException {

    public TooLateToCancelBookingException() {
        super("Too late to cancel booking exception");
    }
}
