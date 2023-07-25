package code.bookings.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class BookingAlreadyCancelledException extends ValidationException {

    public BookingAlreadyCancelledException() {
        super("Booking already cancelled");
    }
}
