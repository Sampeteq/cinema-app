package code.bookings.domain.exceptions;

import code.shared.ValidationException;

public class BookingAlreadyCancelledException extends ValidationException {

    public BookingAlreadyCancelledException() {
        super("Booking already cancelled");
    }
}
