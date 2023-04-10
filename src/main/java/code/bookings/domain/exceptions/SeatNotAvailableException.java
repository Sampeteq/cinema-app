package code.bookings.domain.exceptions;

import code.shared.ValidationException;

public class SeatNotAvailableException extends ValidationException {

    public SeatNotAvailableException() {
        super("Seat not available");
    }
}
