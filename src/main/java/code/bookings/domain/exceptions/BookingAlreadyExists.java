package code.bookings.domain.exceptions;

import code.shared.ValidationException;

public class BookingAlreadyExists extends ValidationException {

    public BookingAlreadyExists() {
        super("Booking already exists");
    }
}
