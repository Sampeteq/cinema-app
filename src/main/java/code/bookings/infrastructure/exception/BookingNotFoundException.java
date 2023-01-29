package code.bookings.infrastructure.exception;

import code.bookings.domain.exception.BookingException;

public class BookingNotFoundException extends BookingException {

    public BookingNotFoundException() {
        super("Booking not found");
    }
}
